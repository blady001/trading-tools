package com.dbrz.trading.alert;

import com.dbrz.trading.exchange.event.TickEvent;
import com.dbrz.trading.notification.Notification;
import com.dbrz.trading.notification.NotificationService;
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
    private final NotificationService notificationService;

    @EventListener
    void process(TickEvent event) {
        log.info("Event received: {}", event);
        getAlertsForEvent(event).stream()
                .filter(alert -> isAlertConditionSatisfied(alert, event))
                .forEach(this::trigger);
    }

    private List<Alert> getAlertsForEvent(TickEvent event) {
        // TODO: use some cache here
        return alertRepository.findByExchangeAndTimeframeAndIsActiveTrue(event.exchange(), event.timeframe());
    }

    private boolean isAlertConditionSatisfied(Alert alert, TickEvent tickEvent) {
        // TODO: test actual condition
        return true;
    }

    private void trigger(Alert alert) {
        var notification = createNotificationFrom(alert);
        notificationService.send(notification);
    }

    private Notification createNotificationFrom(Alert alert) {
        var title = "Trading condition alert";
        var message = String.format("%s | %s | %s", alert.getExchange(), alert.getSymbol(), alert.getTimeframe());
        return new Notification(title, message);
    }
}
