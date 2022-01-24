package com.dbrz.trading.notification.console;

import com.dbrz.trading.notification.Notification;
import com.dbrz.trading.notification.NotificationAdapter;
import com.dbrz.trading.notification.SendingStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class ConsoleNotificationAdapter implements NotificationAdapter {

    @Override
    public SendingStatus send(Notification notification) {
        log.info("Notification: {}", notification);
        return SendingStatus.SUCCESS;
    }
}
