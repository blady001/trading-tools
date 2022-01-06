package com.dbrz.trading.model;

import java.math.BigDecimal;
import java.time.Instant;

public record Candlestick(BigDecimal open,
                          BigDecimal high,
                          BigDecimal low,
                          BigDecimal close,
                          Instant openTime,
                          Instant closeTime) {
}
