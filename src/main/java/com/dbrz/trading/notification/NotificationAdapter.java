package com.dbrz.trading.notification;

interface NotificationAdapter {

    void send(Notification notification) throws NotificationSendingException;
}
