package net.bivrik.fancynotify;

import net.bivrik.fancynotify.notification.gui.TutorialNotification;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.minecraft.client.Minecraft;

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
