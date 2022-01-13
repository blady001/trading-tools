package com.dbrz.trading.analysis.condition;

import com.dbrz.trading.analysis.Candlestick;
import com.dbrz.trading.analysis.indicator.HeikinAshi;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Size;
import java.util.List;

@Component
@Qualifier("heikinAshiEntryCondition")
@RequiredArgsConstructor
class HeikinAshiEntryCondition implements TradingCondition {
    // TODO: Improve condition:
    //  * Last HA Candle should be bullish
    //  * Previous 5 candles should not be bullish
    //  * Bullish candles volume increase
    //  * EMA 10 + 30 - both rising

    @Override
    public boolean isSatisfied(@Size(min = 7) List<Candlestick> series) {
        var haCandles = HeikinAshi.fromSeries(series);
        return wasInDowntrend(haCandles) && lastCandleIsBullish(haCandles);
    }

    private boolean lastCandleIsBullish(List<HeikinAshi.Candle> series) {
        return series.get(1).isBullish();
    }

    private boolean wasInDowntrend(List<HeikinAshi.Candle> series) {
        return series.subList(2, 7).stream().filter(HeikinAshi.Candle::isBullish).toList().isEmpty();
    }
}
