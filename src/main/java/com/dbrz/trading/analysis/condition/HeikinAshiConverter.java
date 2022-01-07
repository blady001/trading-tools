package com.dbrz.trading.analysis.condition;

import com.dbrz.trading.analysis.Candlestick;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
class HeikinAshiConverter {

    List<Candlestick> convertToHACandlesticks(@Size(min = 2) List<Candlestick> candlesticks) {
        LinkedList<Candlestick> candlesticksHA = new LinkedList<>();
        candlesticksHA.add(candlesticks.get(candlesticks.size() - 1));

        for (int i = candlesticks.size() - 2; i >= 0; i--) {
            var previousHA = candlesticksHA.peek();
            var ordinaryCandlestick = candlesticks.get(i);
            candlesticksHA.push(createHACandlestick(ordinaryCandlestick, previousHA));
        }

        candlesticksHA.removeLast();
        return new ArrayList<>(candlesticksHA);
    }

    private Candlestick createHACandlestick(Candlestick current, Candlestick previousHA) {
        BigDecimal closeHA = current.open().add(current.high()).add(current.low()).add(current.close()).divide(new BigDecimal("4"), RoundingMode.UNNECESSARY);
        BigDecimal openHA = previousHA.open().add(previousHA.close()).divide(new BigDecimal("2"), RoundingMode.UNNECESSARY);
        BigDecimal highHA = current.high().max(openHA).max(closeHA);
        BigDecimal lowHA = current.low().min(openHA).min(closeHA);
        return new Candlestick(openHA, highHA, lowHA, closeHA, current.openTime(), current.closeTime());
    }
}
