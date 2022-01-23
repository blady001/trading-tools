package com.dbrz.trading.exchange;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Getter
public enum Timeframe {
    ONE_MINUTE("1m", Duration.ofMinutes(1)),
    FIVE_MINUTES("5m", Duration.ofMinutes(5)),
    HALF_HOUR("30m", Duration.ofMinutes(30)),
    HOUR("1h", Duration.ofHours(1)),
    FOUR_HOURS("4h", Duration.ofHours(4)),
    DAY("1d", Duration.ofDays(1)),
    WEEK("1w", Duration.ofDays(7));

    private final String symbol;
    private final Duration duration;

    public static Timeframe from(String value) {
        return Stream.of(values())
                .filter(t -> t.symbol.equals(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
