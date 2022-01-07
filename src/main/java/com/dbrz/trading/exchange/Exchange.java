package com.dbrz.trading.exchange;

import com.dbrz.trading.analysis.Candlestick;
import com.dbrz.trading.analysis.Timeframe;

import java.time.Instant;
import java.util.List;

public interface Exchange {

    List<Candlestick> getCandlesticks(String symbol, Timeframe timeframe, Integer limit);

    List<Candlestick> getCandlesticks(String symbol, Timeframe timeframe, Instant startTime, Instant endTime);
}
