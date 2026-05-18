package net.bivrik.fancynotify;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bivrik.fancynotify.config.ConfigManager;
import net.bivrik.fancynotify.core.Logger;
import net.bivrik.fancynotify.gui.Notification;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class NotificationManager {
    private static final int MAX_NOTIFICATIONS = 4;
    public static final int PADDING = 2;

    private final Minecraft minecraft;
    private final ConfigManager configManager;
    private final DeltaTracker deltaTracker;
    private final List<Notification> allNotifications = new ArrayList<>();
    private final Deque<Notification> notificationQueue = new ConcurrentLinkedDeque<>();
    private final List<NotificationHolder> currentNotifications = new ArrayList<>();

    public NotificationManager(Minecraft minecraft, ConfigManager configManager) {
        this.minecraft = minecraft;
        this.configManager = configManager;
        this.deltaTracker = minecraft.getTimer();
    }

    public Minecraft getMinecraft() {
        return minecraft;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public void add(Notification notification) {
        if (!notification.shouldDisplay()) {
            return;
        }

        for (Notification n : allNotifications) {
            if (n.tryMerge(notification)) {
                Logger.info("Expanded notification");
                return;
            }
        }

        if (hasCurrentSlots()) {
            currentNotifications.add(new NotificationHolder(notification, getLastPosition()));
            Logger.info("Showing new notification");
        } else {
            notificationQueue.add(notification);
            Logger.info("Added new notification to queue");
        }
        allNotifications.add(notification);
    }

    public void clear() {
        notificationQueue.clear();
        currentNotifications.clear();
        allNotifications.clear();
    }

    private boolean hasCurrentSlots() {
        return currentNotifications.size() < MAX_NOTIFICATIONS;
    }

    public boolean isCurrentEmpty() {
        return currentNotifications.isEmpty();
    }

    public int getAnimationDurationTicks() {
        return 15;
    }

    public void update() {
        if (!isCurrentEmpty()) {
            for (var iterator = currentNotifications.iterator(); iterator.hasNext();) {
                var notificationHolder = iterator.next();

                var notification = notificationHolder.getNotification();
                if (notification.shouldRemove()) {
                    iterator.remove();
                    allNotifications.remove(notification);
                    Logger.info("Removed visible notification");
                    continue;
                }

                notificationHolder.update(deltaTracker.getGameTimeDeltaTicks());
            }

            // uuhh
            int y = PADDING;
            for (var notificationHandler : currentNotifications) {
                notificationHandler.setY(y);
                y += notificationHandler.getNotification().getHeight() + PADDING;
            }
        }

        while (!notificationQueue.isEmpty() && hasCurrentSlots()) {
            Notification next = notificationQueue.pollFirst();
            if (next != null) {
                currentNotifications.add(new NotificationHolder(next, getLastPosition()));
                Logger.info("Showing next notification");
            }
        }
    }

    public <T extends Notification> void remove(Class<T> notificationClass, Object id) {
        for (Notification n : allNotifications) {
            if (id.equals(n.getId()) && notificationClass.isAssignableFrom(n.getClass())) {
                if (notificationQueue.contains(n)) {
                    notificationQueue.remove(n);
                    allNotifications.remove(n);
                } else {
                    n.hide();
                }
                return;
            }
        }
    }

    private float getLastPosition() {
        if (!currentNotifications.isEmpty()) {
            var notificationHolder = currentNotifications.getLast();
            return notificationHolder.getY() + notificationHolder.getNotification().getHeight() + PADDING;
        }

        return PADDING;
    }

    public void render(GuiGraphics guiGraphics) {
        if (currentNotifications.isEmpty() || minecraft.options.hideGui) return;

        for (var notificationHolder : currentNotifications) {
            PoseStack stack = guiGraphics.pose();
            stack.pushPose();
            stack.translate(guiGraphics.guiWidth() - notificationHolder.getNotification().getWidth() - PADDING, 0, 800);
            notificationHolder.render(guiGraphics);
            stack.popPose();
        }
    }

    private static class NotificationHolder {
        private final Notification notification;

        private float timeTicks;
        private float y;
        private float oldY;
        private float newY;
        private float yLastChangedTicks;

        private NotificationHolder(Notification notification, float y) {
            this.notification = notification;

            this.y = y;
            this.newY = y;
        }

        private Notification getNotification() {
            return notification;
        }

        private float getY() {
            return y;
        }

        private void setY(float y) {
            if (y != newY) {
                oldY = this.y;
                newY = y;
                yLastChangedTicks = timeTicks;
                Logger.error("Updated (new " + this.y + ", current " + y + ")");
            }
        }

        private void update(float deltaTicks) {
            notification.update(deltaTicks);
            timeTicks += deltaTicks;

            if (y != newY) {
                y = Easing.OCT_EASE_OUT.lerp(oldY, newY, Keyframe.getProgress(timeTicks, (int) Math.ceil(yLastChangedTicks), 10));
            }
        }

        private void render(GuiGraphics guiGraphics) {
            PoseStack stack = guiGraphics.pose();
            stack.pushPose();
            stack.translate(0, y, 0);
            notification.render(guiGraphics);
            stack.popPose();;
        }
    }
}
