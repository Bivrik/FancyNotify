package net.bivrik.fancynotify;

import net.bivrik.fancynotify.gui.Notification;
import net.bivrik.fancynotify.gui.TutorialNotification;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.TutorialToast;

public class TutorialManager {
    private final Minecraft minecraft;
    private final NotificationManager notificationManager;

    public TutorialManager(Minecraft minecraft, NotificationManager notificationManager) {
        this.minecraft = minecraft;
        this.notificationManager = notificationManager;
    }

    public void addTimed(TutorialNotification notification, int durationTicks) {

    }

    public void removeTimed(TutorialNotification notification) {

    }

    public void tick() {

    }
}
