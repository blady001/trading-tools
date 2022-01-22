package com.dbrz.trading.exchange;


public record TickEvent(ExchangeAdapter exchangeAdapter, Timeframe timeframe) {

}