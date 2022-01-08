package com.dbrz.trading.analysis.condition.heikinashi;

import com.dbrz.trading.analysis.Candlestick;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
class HeikinAshiConverter {

    public List<Candlestick> convertToHACandlesticks(List<Candlestick> candlesticks) {
        LinkedList<Candlestick> candlesticksHA = new LinkedList<>();

        for (int i = candlesticks.size() - 1; i >= 0; i--) {
            Candlestick ordinaryCandlestick = candlesticks.get(i);
            Candlestick previousHA;
            if (candlesticksHA.isEmpty())
                previousHA = ordinaryCandlestick;
            else
                previousHA = candlesticksHA.peek();
            candlesticksHA.push(createHACandlestick(ordinaryCandlestick, previousHA));
        }

        return new ArrayList<>(candlesticksHA);
    }

    private Candlestick createHACandlestick(Candlestick current, Candlestick previousHA) {
        BigDecimal closeHA = current.open().add(current.high()).add(current.low()).add(current.close()).divide(new BigDecimal("4"), 2, RoundingMode.HALF_UP);
        BigDecimal openHA = previousHA.open().add(previousHA.close()).divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP);
        BigDecimal highHA = current.high().max(openHA).max(closeHA);
        BigDecimal lowHA = current.low().min(openHA).min(closeHA);
        return new Candlestick(openHA, highHA, lowHA, closeHA, current.openTime(), current.closeTime());
    }
}
