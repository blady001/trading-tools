package com.dbrz.trading.alert;

import com.dbrz.trading.exchange.Exchange;
import com.dbrz.trading.exchange.ExchangeAdapter;
import com.dbrz.trading.exchange.event.TickEvent;
import com.dbrz.trading.exchange.Timeframe;
import com.dbrz.trading.notification.Notification;
import com.dbrz.trading.notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertProcessingServiceTest {

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private AlertProcessingService alertProcessingService;

    private AlertHelper alertHelper;

    @BeforeEach
    void init() {
        alertHelper = new AlertHelper(alertRepository);
        lenient().when(alertRepository.saveAndFlush(any())).thenAnswer(returnsFirstArg());
    }

    @Test
    void shouldNotTriggerAnyNotificationIfNoAlerts() {
        // given
        givenAlertsFromDb(Collections.emptyList());

        // when
        alertProcessingService.process(givenEvent());

        // then
        thenNoNotificationsWereSent();
    }

    @Test
    void shouldTriggerNotificationForAlert() {
        // given
        var givenAlert = alertHelper.getAlert();
        var givenExpectedNotification = givenExpectedNotificationFrom(givenAlert);
        givenAlertsFromDb(Collections.singletonList(givenAlert));

        // when
        alertProcessingService.process(givenEvent());

        // then
        thenNotificationSent(givenExpectedNotification);
    }

    private void givenAlertsFromDb(List<Alert> alerts) {
        when(alertRepository.findByExchangeAndTimeframeAndIsActiveTrue(any(), any()))
                .thenReturn(alerts);
    }

    private TickEvent givenEvent() {
        return new TickEvent(Exchange.BINANCE, Timeframe.HOUR, Instant.now());
    }

    private Notification givenExpectedNotificationFrom(Alert alert) {
        var title = "Trading condition alert";
        var message = String.format("%s | %s | %s", alert.getExchange(), alert.getSymbol(), alert.getTimeframe());
        return new Notification(title, message);
    }

    private void thenNoNotificationsWereSent() {
        verify(notificationService, never()).send(any());
    }

    private void thenNotificationSent(Notification expectedNotification) {
        verify(notificationService, times(1)).send(expectedNotification);
    }
}
