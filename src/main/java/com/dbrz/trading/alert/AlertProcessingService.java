package com.dbrz.trading.alert;

import com.dbrz.trading.exchange.TickEvent;
import com.dbrz.trading.notification.Notification;
import com.dbrz.trading.notification.NotificationAdapter;
import com.dbrz.trading.notification.SendingStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
class AlertProcessingService {

    private final AlertRepository alertRepository;
    private final NotificationAdapter notificationAdapter;

    @EventListener
    void process(TickEvent event) {
        log.info("Event received: {}", event);
        getAlertsForEvent(event).stream()
                .filter(alert -> isAlertConditionSatisfied(alert, event))
                .forEach(this::trigger);
    }

    private List<Alert> getAlertsForEvent(TickEvent event) {
        // TODO: use some cache here
        return alertRepository.findByExchangeAndTimeframeAndIsActiveTrue(event.exchangeAdapter().getType(), event.timeframe());
    }

    private boolean isAlertConditionSatisfied(Alert alert, TickEvent tickEvent) {
        // TODO: test actual condition
        return true;
    }

    private void trigger(Alert alert) {
        var result = notificationAdapter.send(createNotificationFrom(alert));
        if (result != SendingStatus.SUCCESS) {
            log.warn("Unable to send notification for alert: {}", alert);
        }
    }

    private Notification createNotificationFrom(Alert alert) {
        var title = "Trading condition alert";
        var message = String.format("%s | %s | %s", alert.getExchange(), alert.getSymbol(), alert.getTimeframe());
        return new Notification(title, message);
    }
}
