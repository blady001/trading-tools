package com.dbrz.trading.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationAdapter notificationAdapter;

    public void send(Notification notification) {
        try {
            notificationAdapter.send(notification);
        } catch (NotificationSendingException e) {
            log.error("Failed to send notification: {}, error: {}", notification, e);
        }
    }
}
