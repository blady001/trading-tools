package com.dbrz.trading.exchange;


import java.time.Instant;

public record TickEvent(ExchangeAdapter exchangeAdapter, Timeframe timeframe, Instant createdAt) {

}