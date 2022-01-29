package com.dbrz.trading.notification;

import reactor.core.publisher.Mono;

interface NotificationAdapter {

    Mono<Void> send(Notification notification);
}
