package com.dbrz.trading.alert;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(AlertHelper.class)
public class AlertRepositoryTest {

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private AlertHelper alertHelper;

    @Test
    public void shouldSaveAndFind() {
        // given
        var givenAlert = alertHelper.givenAlertEntity();

        // when
        var actualAlerts = alertRepository.findAll();

        // then
        assertThat(actualAlerts).hasSize(1);
        thenAlertsEqual(actualAlerts.get(0), givenAlert);
    }

    private void thenAlertsEqual(Alert actual, Alert expected) {
        assertThat(actual.getId()).isEqualTo(1L);
        assertThat(actual.getExchange()).isEqualTo(expected.getExchange());
        assertThat(actual.getSymbol()).isEqualTo(expected.getSymbol());
        assertThat(actual.getTrigger()).isEqualTo(expected.getTrigger());
        assertThat(actual.getNotificationMethod()).isEqualTo(expected.getNotificationMethod());
        assertThat(actual.getTimeframe()).isEqualTo(expected.getTimeframe());
        assertThat(actual.getIsActive()).isEqualTo(expected.getIsActive());
    }
}
