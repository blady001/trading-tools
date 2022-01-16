package com.dbrz.trading.alert;

import com.dbrz.trading.analysis.Timeframe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

import java.time.ZoneOffset;
import java.util.List;

@TestComponent
public class AlertHelper {

    @Autowired
    private AlertRepository repository;

    Alert givenAlertEntity() {
        var givenAlert = Alert.builder()
                .exchange("binance")
                .exchangeTimeOffset(ZoneOffset.of("+01:00"))
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
                ZoneOffset.of("+01:00"),
                Timeframe.DAY,
                "heikinAshiEntryCondition",
                1,
                true);
    }

    List<Alert> getAllSavedAlerts() {
        return repository.findAll();
    }
}
