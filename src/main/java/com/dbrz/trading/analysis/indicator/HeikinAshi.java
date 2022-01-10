package com.dbrz.trading.analysis.indicator;

import com.dbrz.trading.analysis.Candlestick;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HeikinAshi {

    public static List<Candlestick> fromSeries(List<Candlestick> series) {
        LinkedList<Candlestick> candlesticksHA = new LinkedList<>();

        for (int i = series.size() - 1; i >= 0; i--) {
            Candlestick ordinaryCandlestick = series.get(i);
            Candlestick previousHA;
            if (candlesticksHA.isEmpty())
                previousHA = ordinaryCandlestick;
            else
                previousHA = candlesticksHA.peek();
            candlesticksHA.push(createHACandlestick(ordinaryCandlestick, previousHA));
        }

        return new ArrayList<>(candlesticksHA);
    }

    private static Candlestick createHACandlestick(Candlestick current, Candlestick previousHA) {
        BigDecimal closeHA = current.open().add(current.high()).add(current.low()).add(current.close()).divide(new BigDecimal("4"), 2, RoundingMode.HALF_UP);
        BigDecimal openHA = previousHA.open().add(previousHA.close()).divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP);
        BigDecimal highHA = current.high().max(openHA).max(closeHA);
        BigDecimal lowHA = current.low().min(openHA).min(closeHA);
        return new Candlestick(openHA, highHA, lowHA, closeHA, current.openTime(), current.closeTime());
    }
}
