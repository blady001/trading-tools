package com.dbrz.trading.alert;

import com.dbrz.trading.analysis.Timeframe;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.OffsetTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AlertRepositoryTest {

    @Autowired
    private AlertRepository alertRepository;

    @Test
    public void shouldSaveAndFind() {
        // given
        var givenAlert = Alert.builder()
                .exchange("binance")
                .exchangeTimeOffset(OffsetTime.of(0, 0, 0, 0, ZoneOffset.UTC))
                .symbol("BTCUSDT")
                .trigger("heikinAshiEntryCondition")
                .notificationMethod(1)
                .timeframe(Timeframe.DAY)
                .isActive(true)
                .build();

        // when
        alertRepository.saveAndFlush(givenAlert);

        // then
        var actualAlerts = alertRepository.findAll();
        assertThat(actualAlerts).hasSize(1);
        thenAlertsEqual(actualAlerts.get(0), givenAlert);
    }

    private void thenAlertsEqual(Alert actual, Alert expected) {
        assertThat(actual.getId()).isEqualTo(1L);
        assertThat(actual.getExchangeTimeOffset()).isEqualTo(expected.getExchangeTimeOffset());
        assertThat(actual.getExchange()).isEqualTo(expected.getExchange());
        assertThat(actual.getSymbol()).isEqualTo(expected.getSymbol());
        assertThat(actual.getTrigger()).isEqualTo(expected.getTrigger());
        assertThat(actual.getNotificationMethod()).isEqualTo(expected.getNotificationMethod());
        assertThat(actual.getTimeframe()).isEqualTo(expected.getTimeframe());
        assertThat(actual.getIsActive()).isEqualTo(expected.getIsActive());
    }
}
