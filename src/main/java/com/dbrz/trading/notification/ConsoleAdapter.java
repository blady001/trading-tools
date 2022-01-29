package com.dbrz.trading.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Qualifier("consoleAdapter")
@Slf4j
class ConsoleAdapter implements NotificationAdapter {

    @Override
    public Mono<Void> send(Notification notification) {
        return Mono.empty().doFirst(() -> log.info("Notifying: {}", notification)).then();
    }
}
