package com.dbrz.trading.alert;

import com.dbrz.trading.analysis.Timeframe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestComponent
public class AlertHelper {

    @Autowired
    private AlertRepository repository;

    Alert givenAlertEntity() {
        var givenAlert = Alert.builder()
                .exchange("binance")
                .symbol("BTCUSDT")
                .trigger("heikinAshiEntryCondition")
                .notificationMethod(1)
                .timeframe(Timeframe.DAY)
                .isActive(true)
                .build();
        return repository.saveAndFlush(givenAlert);
    }

    AlertDto givenAlertDto(Long id) {
        return new AlertDto(
                id,
                "BTCUSDT",
                "binance",
                Timeframe.DAY,
                "heikinAshiEntryCondition",
                1,
                true);
    }

    List<Alert> getAllSavedAlerts() {
        return repository.findAll();
    }

    void thenAlertDoesNotExist(Long id) {
        assertThat(repository.findById(id)).isEmpty();
    }
}
