package net.bivrik.fancynotify.api;

public interface NotificationManager {

    void add(Notification notification);

    <T extends Notification> void remove(Class<T> notificationClass, Object id);

    void clear();

    boolean isEmpty();
}
