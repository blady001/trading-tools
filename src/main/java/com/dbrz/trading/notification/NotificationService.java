package com.dbrz.trading.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Service
@Slf4j
@Validated
public class NotificationService {

    private final NotificationAdapter notificationAdapter;

    public NotificationService(@Qualifier("pushoverAdapter") NotificationAdapter notificationAdapter) {
        this.notificationAdapter = notificationAdapter;
    }

    @Async
    public void send(@Valid Notification notification) {
        notificationAdapter.send(notification)
                .doOnError(throwable -> log.error("Failed to send notification: {}, error: {}", notification, throwable))
                .onErrorResume(throwable -> Mono.empty())
                .block();
    }
}
