package com.dbrz.trading.model;

import java.time.Instant;
import java.util.List;

public interface Exchange {

    List<Candlestick> getCandlesticks(String symbol, Timeframe timeframe, Integer limit);

    List<Candlestick> getCandlesticks(String symbol, Timeframe timeframe, Instant startTime, Instant endTime);
}
