package com.dbrz.trading.exchange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TickEventPublisherTest {

    private TickEventPublisher publisher;

    @Mock
    private ExchangeAdapter exchangeAdapter;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Captor
    private ArgumentCaptor<TickEvent> eventCaptor;

    @BeforeEach
    void init() {
        givenPublisherWithTime(OffsetDateTime.of(2022, 1, 23, 0, 0, 0, 0, ZoneOffset.UTC));
    }

    @Test
    void shouldNotGenerateEventsIfExchangeIsClosed() {
        // given
        givenTradingOpened(false);

        // when
        publisher.generateEvents();

        // then
        verify(applicationEventPublisher, never()).publishEvent(any());
    }

    @ParameterizedTest
    @MethodSource("provideTestDataFor24hExchange")
    void shouldGenerateEvents(OffsetDateTime givenTime, List<Timeframe> expectedTimeframes) {
        // given
        givenExchangeOpened24h();
        givenPublisherWithTime(givenTime);

        // when
        publisher.generateEvents();

        // then
        var expectedEventTimestamp = givenTime.truncatedTo(ChronoUnit.MINUTES).toInstant();
        verifyEventsGenerated(expectedTimeframes, expectedEventTimestamp);
    }

    private void givenPublisherWithTime(OffsetDateTime time) {
        var clock = Clock.fixed(time.toInstant(), ZoneOffset.UTC);
        publisher = new TickEventPublisher(applicationEventPublisher, Collections.singletonList(exchangeAdapter), clock);
    }

    private void givenExchangeOpened24h() {
        givenTradingOpened(true);
        givenExchangeOpenTime(LocalTime.of(0, 0));
        givenExchangeCloseTime(LocalTime.of(0, 0));
        givenExchangeZoneId(ZoneOffset.UTC);
    }

    private void givenTradingOpened(boolean value) {
        when(exchangeAdapter.isTradingOpened()).thenReturn(value);
    }

    private void givenExchangeOpenTime(LocalTime openTime) {
        when(exchangeAdapter.getOpenTime()).thenReturn(openTime);
    }

    private void givenExchangeCloseTime(LocalTime closeTime) {
        when(exchangeAdapter.getCloseTime()).thenReturn(closeTime);
    }

    private void givenExchangeZoneId(ZoneId zoneId) {
        when(exchangeAdapter.getExchangeZoneId()).thenReturn(zoneId);
    }

    private void verifyEventsGenerated(List<Timeframe> expectedTimeframes, Instant expectedTimestamp) {
        verify(applicationEventPublisher, times(expectedTimeframes.size())).publishEvent(eventCaptor.capture());

        var actualEvents = eventCaptor.getAllValues();
        Set<Timeframe> actualTimeframes = actualEvents.stream().map(TickEvent::timeframe).collect(Collectors.toSet());
        assertThat(actualTimeframes).containsAll(expectedTimeframes);

        actualEvents.forEach(event -> assertThat(event.createdAt()).isEqualTo(expectedTimestamp));
    }

    private static Stream<Arguments> provideTestDataFor24hExchange() {
        return Stream.of(
                Arguments.of(
                        OffsetDateTime.of(2022, 1, 23, 0, 0, 0, 0, ZoneOffset.UTC),
                        Arrays.asList(
                                Timeframe.ONE_MINUTE,
                                Timeframe.FIVE_MINUTES,
                                Timeframe.HALF_HOUR,
                                Timeframe.HOUR,
                                Timeframe.FOUR_HOURS,
                                Timeframe.DAY
                        )
                ),
                Arguments.of(
                        OffsetDateTime.of(2022, 1, 1, 1, 0, 0, 0, ZoneOffset.UTC),
                        Arrays.asList(
                                Timeframe.ONE_MINUTE,
                                Timeframe.FIVE_MINUTES,
                                Timeframe.HALF_HOUR,
                                Timeframe.HOUR
                        )
                ),
                Arguments.of(
                        OffsetDateTime.of(2022, 1, 1, 0, 5, 0, 0, ZoneOffset.UTC),
                        Arrays.asList(
                                Timeframe.ONE_MINUTE,
                                Timeframe.FIVE_MINUTES
                        )
                ),
                Arguments.of(
                        OffsetDateTime.of(2022, 1, 1, 0, 30, 0, 0, ZoneOffset.UTC),
                        Arrays.asList(
                                Timeframe.ONE_MINUTE,
                                Timeframe.FIVE_MINUTES,
                                Timeframe.HALF_HOUR
                        )
                ),
                Arguments.of(
                        OffsetDateTime.of(2022, 1, 1, 7, 59, 0, 0, ZoneOffset.UTC),
                        Collections.singletonList(Timeframe.ONE_MINUTE)
                ),
                Arguments.of(
                        OffsetDateTime.of(2022, 1, 1, 8, 0, 0, 0, ZoneOffset.UTC),
                        Arrays.asList(
                                Timeframe.ONE_MINUTE,
                                Timeframe.FIVE_MINUTES,
                                Timeframe.HALF_HOUR,
                                Timeframe.HOUR,
                                Timeframe.FOUR_HOURS
                        )
                ),
                Arguments.of(
                        OffsetDateTime.of(2022, 1, 1, 8, 0, 0, 0, ZoneOffset.of("+01:00")),
                        Arrays.asList(
                                Timeframe.ONE_MINUTE,
                                Timeframe.FIVE_MINUTES,
                                Timeframe.HALF_HOUR,
                                Timeframe.HOUR
                        )
                ),
                Arguments.of(
                        OffsetDateTime.of(2022, 1, 1, 9, 0, 0, 0, ZoneOffset.of("+01:00")),
                        Arrays.asList(
                                Timeframe.ONE_MINUTE,
                                Timeframe.FIVE_MINUTES,
                                Timeframe.HALF_HOUR,
                                Timeframe.HOUR,
                                Timeframe.FOUR_HOURS
                        )
                ),
                Arguments.of(
                        OffsetDateTime.of(2022, 1, 1, 14, 25, 1, 25, ZoneOffset.UTC),
                        Arrays.asList(
                                Timeframe.ONE_MINUTE,
                                Timeframe.FIVE_MINUTES
                        )
                )
        );
    }
}
