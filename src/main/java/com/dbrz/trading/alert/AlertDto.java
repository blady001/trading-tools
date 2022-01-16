package com.dbrz.trading.alert;

import com.dbrz.trading.analysis.Timeframe;

import java.time.ZoneOffset;

public record AlertDto(
        Long id,
        String symbol,
        String exchange,
        ZoneOffset exchangeTimeOffset,
        Timeframe timeframe,
        String trigger,
        Integer notificationMethod,
        Boolean isActive
) {
}
