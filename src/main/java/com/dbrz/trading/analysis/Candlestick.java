package com.dbrz.trading.analysis;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@ToString
@EqualsAndHashCode
@Getter
@RequiredArgsConstructor
public class Candlestick {
    protected final BigDecimal open;
    protected final BigDecimal high;
    protected final BigDecimal low;
    protected final BigDecimal close;
    protected final Instant openTime;
    protected final Instant closeTime;
}
