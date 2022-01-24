package com.dbrz.trading.notification;

public interface NotificationAdapter {

    SendingStatus send(Notification notification);
}
