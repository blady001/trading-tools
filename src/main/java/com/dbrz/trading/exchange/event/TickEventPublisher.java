package com.dbrz.trading.exchange.event;

import com.dbrz.trading.exchange.ExchangeAdapter;
import com.dbrz.trading.exchange.Timeframe;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
class TickEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final List<ExchangeAdapter> exchangeAdapters;
    private final Clock clock;

    @Scheduled(cron = "${exchange.event.cron}")
    void generateEvents() {
        var currentTime = ZonedDateTime.now(clock).truncatedTo(ChronoUnit.MINUTES);
        exchangeAdapters.forEach(exchangeAdapter -> {
            if (exchangeAdapter.isTradingOpened())
                generateEvents(exchangeAdapter, currentTime);
        });
    }

    private void generateEvents(ExchangeAdapter exchangeAdapter, ZonedDateTime currentTime) {
        Stream.of(Timeframe.values())
                .filter(timeframe -> shouldGenerateTickEventFor(timeframe, currentTime, exchangeAdapter))
                .map(timeframe -> new TickEvent(exchangeAdapter.getType(), timeframe, currentTime.toInstant()))
                .forEach(applicationEventPublisher::publishEvent);
    }

    private boolean shouldGenerateTickEventFor(Timeframe timeframe, ZonedDateTime currentTime, ExchangeAdapter exchangeAdapter) {
        var exchangeLocalTime = currentTime.withZoneSameInstant(exchangeAdapter.getExchangeZoneId()).toLocalTime();
        return switch (timeframe) {
            case ONE_MINUTE -> true;
            case WEEK -> false;
            case DAY -> exchangeLocalTime.equals(exchangeAdapter.getCloseTime());
            default -> hasTimeframeElapsed(exchangeAdapter.getOpenTime(), exchangeLocalTime, timeframe);
        };
    }

    private boolean hasTimeframeElapsed(LocalTime start, LocalTime end, Timeframe timeframe) {
        return Duration.between(start, end).toMillis() % timeframe.getDuration().toMillis() == 0;
    }
}
