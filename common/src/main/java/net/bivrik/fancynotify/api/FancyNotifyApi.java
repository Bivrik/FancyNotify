package net.bivrik.fancynotify.api;

public final class FancyNotifyApi {
    private FancyNotifyApi() {}

    private static NotificationManager notificationManagerInstance;

    public static void setNotificationManager(NotificationManager notificationManager) {
        notificationManagerInstance = notificationManager;
    }

    public static NotificationManager getNotificationManager() {
        return notificationManagerInstance;
    }
}
