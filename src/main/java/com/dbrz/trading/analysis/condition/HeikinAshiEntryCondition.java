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

    @Override
    public boolean isSatisfied(List<Candlestick> data) {
        return false;
    }
}
