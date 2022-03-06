package com.dbrz.trading.strategy.factory;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Strategy;

import javax.validation.constraints.NotNull;

public interface StrategyFactory {

    Strategy build(@NotNull BarSeries barSeries);
}
