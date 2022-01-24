package com.dbrz.trading.alert;

import com.dbrz.trading.exchange.Exchange;
import com.dbrz.trading.exchange.Timeframe;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record AlertDto(
        Long id,
        @NotEmpty String symbol,
        @NotNull Exchange exchange,
        @NotNull Timeframe timeframe,
        @NotEmpty String trigger,
        @NotNull Integer notificationMethod,
        @NotNull Boolean isActive
) {
}
