package com.dbrz.trading.alert;

import com.dbrz.trading.analysis.Timeframe;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.ZoneOffset;

public record AlertDto(
        Long id,
        @NotEmpty String symbol,
        @NotEmpty String exchange,
        @NotNull ZoneOffset exchangeTimeOffset,
        @NotNull Timeframe timeframe,
        @NotEmpty String trigger,
        @NotNull Integer notificationMethod,
        @NotNull Boolean isActive
) {
}
