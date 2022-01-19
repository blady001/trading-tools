package com.dbrz.trading.exchange;

import java.time.Instant;
import java.util.List;

public interface ExchangeAdapter {

    List<Candlestick> getCandlesticks(String symbol, Timeframe timeframe, Integer limit);

    List<Candlestick> getCandlesticks(String symbol, Timeframe timeframe, Instant startTime, Instant endTime);
}
