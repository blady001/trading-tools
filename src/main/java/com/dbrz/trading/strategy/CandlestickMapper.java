package com.dbrz.trading.strategy;

import com.dbrz.trading.exchange.Candlestick;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseBar;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;

@Component
@RequiredArgsConstructor
class CandlestickMapper {

    private final Clock clock;

    Bar candlestickToBar(Candlestick candlestick) {
        return new BaseBar(
                Duration.between(candlestick.openTime(), candlestick.closeTime()),
                candlestick.closeTime().atZone(clock.getZone()),
                candlestick.open(),
                candlestick.high(),
                candlestick.low(),
                candlestick.close(),
                BigDecimal.valueOf(0)  // TODO: Add volume
        );
    }
}