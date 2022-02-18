package com.dbrz.trading.exchange.event;


import com.dbrz.trading.exchange.Exchange;
import com.dbrz.trading.exchange.Timeframe;

import java.time.Instant;

public record TickEvent(Exchange exchange, Timeframe timeframe, Instant createdAt) {

}