package net.bivrik.fancynotify.api;

import net.bivrik.fancynotify.api.gui.notification.NotificationManager;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public final class FancyNotifyApi {
    private FancyNotifyApi() {}

    private static NotificationManager notificationManagerInstance;

    public static void setNotificationManager(NotificationManager notificationManager) {
        if (notificationManagerInstance != null) {
            throw new IllegalStateException("Notification Manager is already set!");
        }
        notificationManagerInstance = notificationManager;
    }

    public static NotificationManager getNotificationManager() {
        if (notificationManagerInstance == null) {
            throw new IllegalStateException("Notification Manager is null. You cannot access it this soon");
        }
        return notificationManagerInstance;
    }
}
