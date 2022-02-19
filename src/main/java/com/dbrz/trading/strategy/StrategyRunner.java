package com.dbrz.trading.strategy;

import com.dbrz.trading.exchange.ExchangeAdapter;
import com.dbrz.trading.exchange.Timeframe;
import com.dbrz.trading.exchange.event.TickEvent;
import com.dbrz.trading.notification.Notification;
import com.dbrz.trading.notification.NotificationService;
import com.dbrz.trading.strategy.factory.StrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.ta4j.core.*;

import java.util.List;

@Service
@Slf4j
class StrategyRunner {

    private static final int BAR_SERIES_LENGTH = 100;
    private static final String SYMBOL = "BTCUSDT";
    private static final Timeframe TIMEFRAME = Timeframe.FIVE_MINUTES;

    private final Strategy strategy;
    private final NotificationService notificationService;
    private final ExchangeAdapter exchangeAdapter;
    private final BarSeries barSeries;
    private final CandlestickMapper candlestickMapper;

    public StrategyRunner(@Qualifier("simpleRsiStrategy") StrategyFactory strategyFactory,
                          @Qualifier("binanceAdapter") ExchangeAdapter exchangeAdapter,
                          NotificationService notificationService,
                          CandlestickMapper candlestickMapper) {
        this.notificationService = notificationService;
        this.exchangeAdapter = exchangeAdapter;
        this.candlestickMapper = candlestickMapper;
        this.barSeries = initBarSeries();
        this.strategy = strategyFactory.build(this.barSeries);
    }

    private BarSeries initBarSeries() {
        return new BaseBarSeriesBuilder()
                .withName(SYMBOL)
                .withMaxBarCount(BAR_SERIES_LENGTH)
                .withBars(getLatestBars(BAR_SERIES_LENGTH))
                .build();
    }

    private List<Bar> getLatestBars(int limit) {
        return exchangeAdapter.getCandlesticks(SYMBOL, TIMEFRAME, limit).stream()
                .map(candlestickMapper::candlestickToBar)
                .toList();
    }

    @EventListener
    synchronized void process(TickEvent tickEvent) {
        if (shouldRunStrategy(tickEvent)) {
            log.debug("running");
            runStrategy();
        }
    }

    private boolean shouldRunStrategy(TickEvent tickEvent) {
        return tickEvent.timeframe() == TIMEFRAME && tickEvent.exchange() == exchangeAdapter.getType();
    }

    private void runStrategy() {
        updateBarSeries();
        if (shouldEnter()) {
            enter();
        } else if (shouldExit()) {
            exit();
        }
    }

    private void updateBarSeries() {
        getLatestBars(1).forEach(barSeries::addBar);
    }

    private boolean shouldEnter() {
        return strategy.shouldEnter(barSeries.getEndIndex());
    }

    private boolean shouldExit() {
        return strategy.shouldExit(barSeries.getEndIndex());
    }

    private void enter() {
        notificationService.send(new Notification("BUY", SYMBOL));
    }

    private void exit() {
        notificationService.send(new Notification("SELL", SYMBOL));
    }
}
