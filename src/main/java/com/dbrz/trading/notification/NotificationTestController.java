package com.dbrz.trading.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("notification")
@RequiredArgsConstructor
class NotificationTestController {

    private final NotificationService notificationService;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public void sendNotification(@RequestParam String message) {
        var notification = new Notification("Test title", message);
        notificationService.send(notification);
    }
}
