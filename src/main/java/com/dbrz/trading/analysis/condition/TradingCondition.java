package com.dbrz.trading.analysis.condition;

import com.dbrz.trading.analysis.Candlestick;

import java.util.List;

public interface TradingCondition {

    boolean isSatisfied(List<Candlestick> data);
}
