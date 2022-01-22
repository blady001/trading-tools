package com.dbrz.trading.exchange;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

public interface ExchangeAdapter {

    List<Candlestick> getCandlesticks(String symbol, Timeframe timeframe, Integer limit);

    List<Candlestick> getCandlesticks(String symbol, Timeframe timeframe, Instant startTime, Instant endTime);

    Exchange getType();

    boolean isTradingOpened();

    LocalTime getOpenTime();

    LocalTime getCloseTime();

    ZoneId getExchangeZoneId();
}
