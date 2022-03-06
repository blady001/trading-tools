package com.dbrz.trading.alert;

import com.dbrz.trading.exchange.Exchange;
import com.dbrz.trading.exchange.Timeframe;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(AlertHelper.class)
class AlertRepositoryTest {

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private AlertHelper alertHelper;

    @Test
    void shouldSaveAndFind() {
        // given
        var givenAlert = alertHelper.getAlert();

        // when
        var actualAlerts = alertRepository.findAll();

        // then
        assertThat(actualAlerts).hasSize(1);
        thenAlertsEqual(actualAlerts.get(0), givenAlert);
    }

    @Test
    void shouldReturnOnlyActiveAlerts() {
        // given
        var givenActiveAlert = alertHelper.withIsActive(true).getAlert();
        var givenInactiveAlert = alertHelper.withIsActive(false).getAlert();

        // when
        var actualAlerts = alertRepository.findByIsActiveTrue();

        // then
        assertThat(actualAlerts).hasSize(1)
                .doesNotContain(givenInactiveAlert)
                .contains(givenActiveAlert);
    }

    @Test
    void shouldReturnActiveAlertsForGivenExchangeAndTimeframe() {
        // given
        var givenExchange = Exchange.BINANCE;
        var givenTimeframe = Timeframe.FOUR_HOURS;
        var givenAlert1 = alertHelper.withExchange(givenExchange)
                .withTimeframe(givenTimeframe)
                .getAlert();
        var givenAlert2 = alertHelper.getAlert();
        var givenAlert3 = alertHelper.withIsActive(false).getAlert();
        var givenAlert4 = alertHelper.withIsActive(true)
                .withTimeframe(Timeframe.ONE_MINUTE)
                .getAlert();

        // when
        var actualAlerts = alertRepository.findByExchangeAndTimeframeAndIsActiveTrue(givenExchange, givenTimeframe);

        // then
        assertThat(actualAlerts).hasSize(2)
                .doesNotContain(givenAlert3, givenAlert4)
                .contains(givenAlert1, givenAlert2);
    }

    private void thenAlertsEqual(Alert actual, Alert expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getExchange()).isEqualTo(expected.getExchange());
        assertThat(actual.getSymbol()).isEqualTo(expected.getSymbol());
        assertThat(actual.getTrigger()).isEqualTo(expected.getTrigger());
        assertThat(actual.getNotificationMethod()).isEqualTo(expected.getNotificationMethod());
        assertThat(actual.getTimeframe()).isEqualTo(expected.getTimeframe());
        assertThat(actual.getIsActive()).isEqualTo(expected.getIsActive());
    }
}
