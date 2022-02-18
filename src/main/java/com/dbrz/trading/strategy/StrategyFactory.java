package com.dbrz.trading.strategy;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Strategy;

interface StrategyFactory {

    Strategy build(BarSeries barSeries);
}
