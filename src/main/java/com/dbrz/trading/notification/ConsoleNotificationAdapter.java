package com.dbrz.trading.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class ConsoleNotificationAdapter implements NotificationAdapter {

    @Override
    public void send(Notification notification) throws NotificationSendingException {
        log.info("Notifying: {}", notification);
    }
}
