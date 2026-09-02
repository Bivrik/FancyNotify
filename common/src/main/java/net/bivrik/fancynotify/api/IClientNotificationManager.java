package net.bivrik.fancynotify.api;

import net.bivrik.fancynotify.notification.Notification;

public interface IClientNotificationManager {
    void add(Notification notification);
    <T extends Notification> void remove(Class<T> notificationClass, Object id);
    void clear();
}
