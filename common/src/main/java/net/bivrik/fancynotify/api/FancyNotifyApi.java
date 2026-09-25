package net.bivrik.fancynotify.api;

public final class FancyNotifyApi {
    private FancyNotifyApi() {}

    private static NotificationManager notificationManagerInstance;

    public static void setNotificationManager(NotificationManager notificationManager) {
        if (notificationManagerInstance != null) {
            throw new IllegalStateException("NotificationManager is already set!");
        }
        notificationManagerInstance = notificationManager;
    }

    public static NotificationManager getNotificationManager() {
        return notificationManagerInstance;
    }
}
