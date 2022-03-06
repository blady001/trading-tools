package com.dbrz.trading.strategy.factory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;

import javax.validation.constraints.NotNull;

@Component
@Qualifier("simpleRsiStrategy")
@Validated
class SimpleRsiStrategy implements StrategyFactory {

    private static final int RSI_LENGTH = 14;

    @Override
    public Strategy build(@NotNull BarSeries barSeries) {
        ClosePriceIndicator closePrice = new ClosePriceIndicator(barSeries);
        RSIIndicator rsiIndicator = new RSIIndicator(closePrice, RSI_LENGTH);

        Rule entryRule = new CrossedDownIndicatorRule(rsiIndicator, DecimalNum.valueOf(40));
        Rule exitRule = new CrossedUpIndicatorRule(rsiIndicator, DecimalNum.valueOf(60));

        return new BaseStrategy(entryRule, exitRule);
    }
}
