package com.dbrz.trading.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class NotificationService {

    private final NotificationAdapter notificationAdapter;

    public NotificationService(@Qualifier("consoleAdapter") NotificationAdapter notificationAdapter) {
        this.notificationAdapter = notificationAdapter;
    }

    public Mono<Void> send(Notification notification) {
        return notificationAdapter.send(notification)
                .doOnError(throwable -> log.error("Failed to send notification: {}, error: {}", notification, throwable))
                .onErrorResume(throwable -> Mono.empty());
    }
}
