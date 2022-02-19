package com.dbrz.trading.strategy.factory;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Strategy;

public interface StrategyFactory {

    Strategy build(BarSeries barSeries);
}
