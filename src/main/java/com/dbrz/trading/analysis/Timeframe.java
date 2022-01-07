package com.dbrz.trading.analysis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Timeframe {
    ONE_MINUTE("1m"),
    FIVE_MINUTES("5m"),
    HALF_HOUR("30m"),
    HOUR("1h"),
    FOUR_HOURS("4h"),
    DAY("1d"),
    WEEK("1w");

    private final String symbol;
}
