package com.dbrz.trading.exchange;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
class TickEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final List<ExchangeAdapter> exchangeAdapters;
    private final Clock clock;

    // TODO: Start with testing
    void process() {
        var currentTime = ZonedDateTime.now(clock).truncatedTo(ChronoUnit.MINUTES);
        exchangeAdapters.forEach(exchangeAdapter -> {
            if (exchangeAdapter.isTradingOpened())
                generateEvent(exchangeAdapter, currentTime);
        });
    }

    private void generateEvent(ExchangeAdapter exchangeAdapter, ZonedDateTime currentTime) {
        determineTickTimeframe(exchangeAdapter, currentTime)
                .map(timeframe -> new TickEvent(exchangeAdapter, timeframe))
                .ifPresent(applicationEventPublisher::publishEvent);
    }

    private Optional<Timeframe> determineTickTimeframe(ExchangeAdapter exchangeAdapter, ZonedDateTime currentTime) {
        return getTimeframesDescending()
                .filter(timeframe -> isTimeframeApplicable(timeframe, currentTime, exchangeAdapter))
                .findFirst();
    }

    private Stream<Timeframe> getTimeframesDescending() {
        return Stream.of(Timeframe.values())
                .sorted(new TimeframeComparator().reversed());
    }

    private boolean isTimeframeApplicable(Timeframe timeframe, ZonedDateTime currentTime, ExchangeAdapter exchangeAdapter) {
        var exchangeLocalTime = currentTime.withZoneSameInstant(exchangeAdapter.getExchangeZoneId()).toLocalTime();
        return switch (timeframe) {
            case WEEK -> false;
            case DAY -> exchangeLocalTime.equals(exchangeAdapter.getCloseTime());
            default -> hasTimeframeElapsed(exchangeAdapter.getOpenTime(), exchangeLocalTime, timeframe);
        };
    }

    private boolean hasTimeframeElapsed(LocalTime start, LocalTime end, Timeframe timeframe) {
        return Duration.between(start, end).toMillis() % timeframe.getDuration().toMillis() == 0;
    }
}
