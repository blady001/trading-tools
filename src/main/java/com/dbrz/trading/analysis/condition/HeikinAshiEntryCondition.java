package com.dbrz.trading.analysis.condition;

import com.dbrz.trading.analysis.Candlestick;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Qualifier("heikinAshiEntryCondition")
@RequiredArgsConstructor
class HeikinAshiEntryCondition implements TradingCondition {

    private final HeikinAshiConverter heikinAshiConverter;

    @Override
    public boolean isSatisfied(List<Candlestick> data) {
        List<Candlestick> haCandlesticks = heikinAshiConverter.convertToHACandlesticks(data);

        return false;
    }
}
