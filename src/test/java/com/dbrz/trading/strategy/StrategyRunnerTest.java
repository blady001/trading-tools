package com.dbrz.trading.strategy;

import com.dbrz.trading.exchange.Exchange;
import com.dbrz.trading.exchange.ExchangeAdapter;
import com.dbrz.trading.exchange.Timeframe;
import com.dbrz.trading.exchange.event.TickEvent;
import com.dbrz.trading.notification.Notification;
import com.dbrz.trading.notification.NotificationService;
import com.dbrz.trading.strategy.factory.StrategyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ta4j.core.Strategy;

import java.time.Clock;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StrategyRunnerTest {

    @Mock
    private StrategyFactory strategyFactory;

    @Mock
    private ExchangeAdapter exchangeAdapter;

    @Mock
    private NotificationService notificationService;

    @Mock
    private Clock clock;

    @Mock
    private Strategy strategy;

    private StrategyRunner strategyRunner;

    @Captor
    private ArgumentCaptor<Notification> notificationCaptor;

    @BeforeEach
    void init() {
        CandlestickMapper candlestickMapper = new CandlestickMapper(clock);
        when(strategyFactory.build(any())).thenReturn(strategy);
        strategyRunner = new StrategyRunner(
                strategyFactory,
                exchangeAdapter,
                notificationService,
                candlestickMapper
        );
    }

    @Test
    void shouldNotifyAboutBuying() {
        // given
        var givenExchange = Exchange.BINANCE;
        var givenTickEvent = givenTickEvent(givenExchange, StrategyRunner.TIMEFRAME);

        when(exchangeAdapter.getType()).thenReturn(givenExchange);
        when(strategy.shouldEnter(anyInt())).thenReturn(true);

        // when
        strategyRunner.process(givenTickEvent);

        // then
        verify(exchangeAdapter, times(1)).getCandlesticks(
                StrategyRunner.SYMBOL, StrategyRunner.TIMEFRAME, 1);
        verify(strategy, times(1)).shouldEnter(anyInt());
        verify(strategy, never()).shouldExit(anyInt());
        verify(notificationService, times(1)).send(notificationCaptor.capture());
        assertThat(notificationCaptor.getAllValues()).hasSize(1)
                .contains(new Notification("BUY", StrategyRunner.SYMBOL));
    }

    @Test
    void shouldNotifyAboutSelling() {
        // given
        var givenExchange = Exchange.BINANCE;
        var givenTickEvent = givenTickEvent(givenExchange, StrategyRunner.TIMEFRAME);

        when(exchangeAdapter.getType()).thenReturn(givenExchange);
        when(strategy.shouldEnter(anyInt())).thenReturn(false);
        when(strategy.shouldExit(anyInt())).thenReturn(true);

        // when
        strategyRunner.process(givenTickEvent);

        // then
        verify(exchangeAdapter, times(1)).getCandlesticks(
                StrategyRunner.SYMBOL, StrategyRunner.TIMEFRAME, 1);
        verify(strategy, times(1)).shouldEnter(anyInt());
        verify(strategy, times(1)).shouldExit(anyInt());
        verify(notificationService, times(1)).send(notificationCaptor.capture());
        assertThat(notificationCaptor.getAllValues()).hasSize(1)
                .contains(new Notification("SELL", StrategyRunner.SYMBOL));
    }

    @Test
    void shouldNotReactUponUnrelatedTickEvent() {
        // given
        var givenExchange = Exchange.BINANCE;
        var givenTickEvent = givenTickEvent(givenExchange, Timeframe.WEEK);

        // then
        strategyRunner.process(givenTickEvent);

        // then
        verify(exchangeAdapter, never()).getCandlesticks(StrategyRunner.SYMBOL, StrategyRunner.TIMEFRAME, 1);
        verify(strategy, never()).shouldEnter(anyInt());
        verify(strategy, never()).shouldExit(anyInt());
        verify(notificationService, never()).send(any());
    }

    private TickEvent givenTickEvent(Exchange exchange, Timeframe timeframe) {
        return new TickEvent(exchange, timeframe, Instant.now());
    }
}
