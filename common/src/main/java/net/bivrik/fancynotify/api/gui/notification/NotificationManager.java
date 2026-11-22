package net.bivrik.fancynotify.api.gui.notification;

import net.bivrik.fancynotify.api.FancyNotifyApi;
import org.jetbrains.annotations.ApiStatus;

/**
 * Entry point for showing and managing notifications from other mods
 * <br>
 * Get an instance via {@link FancyNotifyApi#getNotificationManager()}
 */
@ApiStatus.Experimental
public interface NotificationManager {

    /**
     * Adds a notification, showing it immediately if there's a free slot, queueing it otherwise, to show it as soon as the next slot frees
     * <br>
     * If it's an {@link ExpandableNotification} and a matching notification is already showing or queued, it's expanded into that one instead
     *
     * @param notification the notification to add
     */
    void add(Notification notification);

    /**
     * Removes a notification from the overall pool
     *
     * @param notificationClass the notification's class
     * @param id the notification's {@link Notification#getId()} ({@link NotificationIdentifier})
     */
    void remove(Class<? extends Notification> notificationClass, NotificationIdentifier id);

    /**
     * Removes absolutely all notifications from the overall pool, both showing and queued
     */
    void clear();

    /**
     * Checks if there are any notifications in the overall pool
     *
     * @return true if there's nothing showing or queued, false otherwise
     */
    boolean isEmpty();
}
