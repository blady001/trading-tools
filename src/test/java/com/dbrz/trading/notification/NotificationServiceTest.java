package com.dbrz.trading.notification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationAdapter notificationAdapter;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void shouldCallNotificationAdapter() {
        // given
        var notification = new Notification("title", "message");
        when(notificationAdapter.send(any())).thenReturn(Mono.empty());

        // when
        var result = notificationService.send(notification);

        // then
        StepVerifier.create(result)
                .verifyComplete();
        verify(notificationAdapter, times(1)).send(notification);
    }

    @Test
    void shouldHandleAdapterException() {
        // given
        var notification = new Notification("title", "message");
        when(notificationAdapter.send(any())).thenReturn(Mono.error(new IllegalStateException()));

        // when
        var result = notificationService.send(notification);

        // then
        StepVerifier.create(result)
                        .verifyComplete();
        verify(notificationAdapter, times(1)).send(notification);
    }
}
