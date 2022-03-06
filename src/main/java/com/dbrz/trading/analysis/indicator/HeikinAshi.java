package com.dbrz.trading.analysis.indicator;

import com.dbrz.trading.analysis.Candlestick;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HeikinAshi {

    public static class Candle extends Candlestick {
        public Candle(BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close, Instant openTime, Instant closeTime) {
            super(open, high, low, close, openTime, closeTime);
        }

        public boolean isBullish() {
            return close.compareTo(open) > 0 && open.equals(low);
        }

        public boolean isBearish() {
            return open.compareTo(close) > 0 && open.equals(high);
        }
    }

    public static List<Candle> fromSeries(List<Candlestick> series) {
        LinkedList<Candle> candlesticksHA = new LinkedList<>();

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

    private static Candle createHACandlestick(Candlestick current, Candlestick previousHA) {
        BigDecimal closeHA = current.getOpen().add(current.getHigh()).add(current.getLow()).add(current.getClose()).divide(new BigDecimal("4"), 2, RoundingMode.HALF_UP);
        BigDecimal openHA = previousHA.getOpen().add(previousHA.getClose()).divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP);
        BigDecimal highHA = current.getHigh().max(openHA).max(closeHA);
        BigDecimal lowHA = current.getLow().min(openHA).min(closeHA);
        return new Candle(openHA, highHA, lowHA, closeHA, current.getOpenTime(), current.getCloseTime());
    }
}
