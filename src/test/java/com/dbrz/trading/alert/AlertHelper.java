package com.dbrz.trading.alert;

import com.dbrz.trading.exchange.Exchange;
import com.dbrz.trading.exchange.Timeframe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestComponent
public class AlertHelper {

    @Autowired
    private AlertRepository repository;

    private final Exchange defaultExchange = Exchange.BINANCE;
    private final String defaultSymbol = "BTCUSDT";
    private final String defaultTrigger = "heikinAshiEntryCondition";
    private final int defaultNotificationMethod = 1;
    private final Timeframe defaultTimeframe = Timeframe.DAY;
    private final boolean defaultIsActive = true;

    private Exchange givenExchange = defaultExchange;
    private String givenSymbol = defaultSymbol;
    private String givenTrigger = defaultTrigger;
    private int givenNotificationMethod = defaultNotificationMethod;
    private Timeframe givenTimeframe = defaultTimeframe;
    private boolean givenIsActive = defaultIsActive;

    Alert getAlert() {
        var givenAlert = Alert.builder()
                .exchange(givenExchange)
                .symbol(givenSymbol)
                .trigger(givenTrigger)
                .notificationMethod(givenNotificationMethod)
                .timeframe(givenTimeframe)
                .isActive(givenIsActive)
                .build();
        return repository.saveAndFlush(givenAlert);
    }

    AlertDto givenAlertDto(Long id) {
        return new AlertDto(
                id,
                givenSymbol,
                givenExchange,
                givenTimeframe,
                givenTrigger,
                givenNotificationMethod,
                givenIsActive);
    }

    List<Alert> getAllSavedAlerts() {
        return repository.findAll();
    }

    void thenAlertDoesNotExist(Long id) {
        assertThat(repository.findById(id)).isEmpty();
    }

    public void reset() {
        givenExchange = defaultExchange;
        givenTimeframe = defaultTimeframe;
        givenIsActive = defaultIsActive;
        givenSymbol = defaultSymbol;
        givenNotificationMethod = defaultNotificationMethod;
        givenTrigger = defaultTrigger;
    }

    public AlertHelper withExchange(Exchange value) {
        givenExchange = value;
        return this;
    }

    public AlertHelper withSymbol(String value) {
        givenSymbol = value;
        return this;
    }

    public AlertHelper withTrigger(String value) {
        givenTrigger = value;
        return this;
    }

    public AlertHelper withNotificationMethod(int value) {
        givenNotificationMethod = value;
        return this;
    }

    public AlertHelper withIsActive(boolean value) {
        givenIsActive = value;
        return this;
    }

    public AlertHelper withTimeframe(Timeframe value) {
        givenTimeframe = value;
        return this;
    }
}
