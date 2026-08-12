package net.bivrik.fancynotify;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bivrik.fancynotify.config.ConfigManager;
import net.bivrik.fancynotify.config.GeneralConfig;
import net.bivrik.fancynotify.core.Log;
import net.bivrik.fancynotify.gui.Notification;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class NotificationManager {
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

    public void add(Notification newNotification) {
        if (!newNotification.shouldDisplay()) {
            return;
        }

        for (Notification n : allNotifications) {
            if (n.tryMerge(newNotification)) {
                Log.info("Expanded " + newNotification.getClass().getSimpleName());
                return;
            }
        }

        if (hasCurrentSlots()) {
            Position position = computePosition(newNotification);
            currentNotifications.add(new NotificationHolder(newNotification, position.x(), position.y()));
            Log.info("Showing new " + newNotification.getClass().getSimpleName());
        } else {
            notificationQueue.add(newNotification);
            Log.info("Added new " + newNotification.getClass().getSimpleName() + " to queue");
        }
        allNotifications.add(newNotification);
    }

    public void clear() {
        notificationQueue.clear();
        currentNotifications.clear();
        allNotifications.clear();
    }

    private boolean hasCurrentSlots() {
        return currentNotifications.size() < configManager.getGeneralConfig().maxAmount.get();
    }

    public boolean isCurrentEmpty() {
        return currentNotifications.isEmpty();
    }

    private void arrangeNotifications() {
        GeneralConfig config = configManager.getGeneralConfig();
        GeneralConfig.Anchor anchor = config.anchor.get();
        boolean isVertical = config.orientation.get() == GeneralConfig.Orientation.VERTICAL;
        int x = 0;
        int y = 0;
        for (var h : currentNotifications) {
            int width = h.getWidth();
            int height = h.getHeight();

            int xOffset = anchor.isLeft() ? (isVertical ? 0 : -width) : (isVertical ? -width : 0);
            int yOffset = anchor.isTop() ? (isVertical ? -height : 0) : (isVertical ? 0 : -height);

            x += isVertical ? 0 : (anchor.isLeft() ? width : -width);
            y += isVertical ? (anchor.isTop() ? height : -height) : 0;

            h.setX(x + xOffset);
            h.setY(y + yOffset);

            int padding = configManager.getGeneralConfig().padding.get();
            if (isVertical) {
                y += anchor.isTop() ? padding : -padding;
            } else {
                x += anchor.isLeft() ? padding : -padding;
            }
        }
    }

    // Why is it so much conditions I need to change this BRO :sob:
    private Position computePosition(Notification notification) {
        GeneralConfig config = configManager.getGeneralConfig();
        GeneralConfig.Anchor anchor = config.anchor.get();
        boolean isVertical = config.orientation.get() == GeneralConfig.Orientation.VERTICAL;
        int padding = config.padding.get();

        int x = 0;
        int y = 0;
        for (var h : currentNotifications) {
            int width = h.getWidth();
            int height = h.getHeight();

            x += isVertical ? 0 : (anchor.isLeft() ? width : -width);
            y += isVertical ? (anchor.isTop() ? height : -height) : 0;

            if (isVertical) {
                y += anchor.isTop() ? padding : -padding;
            } else {
                x += anchor.isLeft() ? padding : -padding;
            }
        }

        int width = notification.getWidth();
        int height = notification.getHeight();
        int posX, posY;

        if (isVertical) {
            posX = anchor.isLeft() ? 0 : -width;
            posY = anchor.isTop() ? y : y - height;
        } else {
            posX = anchor.isLeft() ? x : x - width;
            posY = anchor.isTop() ? 0 : -height;
        }

        return new Position(posX, posY);
    }

    private record Position(int x, int y) {}

    public void update() {
        if (!isCurrentEmpty()) {
            float deltaTicks = deltaTracker.getGameTimeDeltaTicks();
            for (var iterator = currentNotifications.iterator(); iterator.hasNext();) {
                var notificationHolder = iterator.next();

                var notification = notificationHolder.getNotification();
                if (notification.shouldRemove()) {
                    iterator.remove();
                    allNotifications.remove(notification);
                    Log.info("Removed visible notification");
                    continue;
                }

                GeneralConfig config = configManager.getGeneralConfig();
                GeneralConfig.Anchor anchor = config.anchor.get();
                int padding = config.padding.get();
                float anchorX = anchor.isLeft() ? padding : minecraft.getWindow().getGuiScaledWidth() - padding;
                float anchorY = anchor.isTop() ? padding : minecraft.getWindow().getGuiScaledHeight() - padding;
                notificationHolder.update(deltaTicks, anchorX, anchorY);
            }

            arrangeNotifications();
        }

        while (!notificationQueue.isEmpty() && hasCurrentSlots()) {
            Notification next = notificationQueue.pollFirst();
            if (next != null) {
                Position position = computePosition(next);
                currentNotifications.add(new NotificationHolder(next, position.x(), position.y()));
                Log.info("Showing next notification");
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

    public void render(GuiGraphics guiGraphics) {
        if (currentNotifications.isEmpty() || minecraft.options.hideGui) return;

        PoseStack stack = guiGraphics.pose();
        stack.pushPose();
        GeneralConfig config = configManager.getGeneralConfig();
        GeneralConfig.Anchor anchor = config.anchor.get();
        int padding = config.padding.get();

        if (config.debug.get()) {
            stack.translate(guiGraphics.guiWidth() / 2.0, guiGraphics.guiHeight() / 2.0, 800);

            guiGraphics.fill(-500, 0, 500, 1, -58254424);
            guiGraphics.fill(0, -500, 1, 500, -58254424);

            guiGraphics.fill(-500, -1, 500, 0, -812254424);
            guiGraphics.fill(-1, -500, 0, 500, -812254424);

            guiGraphics.drawString(minecraft.font, "(-1, 1)", -37, 6, -1);
            guiGraphics.drawString(minecraft.font, "(1, -1)", 6, -13, -1);
            guiGraphics.drawString(minecraft.font, "(0, 0)", -13, -3, -1);
        } else {
            stack.translate(anchor.isLeft() ? padding : guiGraphics.guiWidth() - padding, anchor.isTop() ? padding : guiGraphics.guiHeight() - padding, 800);
        }

        for (var notificationHolder : currentNotifications) {
            notificationHolder.render(guiGraphics);
        }

        stack.popPose();
    }

    private static class NotificationHolder {
        private final static int ANIMATION_SPEED = 20;

        private final Notification notification;

        private float timeTicks;

        private float x;
        private float oldX;
        private float newX;
        private float xLastChangedTicks;

        private float y;
        private float oldY;
        private float newY;
        private float yLastChangedTicks;

        private NotificationHolder(Notification notification, float x, float y) {
            this.notification = notification;

            this.x = x;
            this.newX = x;
            this.y = y;
            this.newY = y;
        }

        private Notification getNotification() {
            return notification;
        }

        private int getWidth() {
            return notification.getWidth();
        }

        private void setX(float x) {
            if (x != newX) {
                oldX = this.x;
                newX = x;
                xLastChangedTicks = timeTicks;
                Log.info("Updated X (new " + this.x + ", current " + x + ")");
            }
        }

        private int getHeight() {
            return notification.getHeight();
        }

        private void setY(float y) {
            if (y != newY) {
                oldY = this.y;
                newY = y;
                yLastChangedTicks = timeTicks;
                Log.info("Updated Y (new " + this.y + ", current " + y + ")");
            }
        }

        private void update(float deltaTicks, float anchorX, float anchorY) {
            notification.update(deltaTicks, anchorX + x, anchorY + y);
            timeTicks += deltaTicks;

            if (x != newX) {
                x = Easing.OCT_EASE_OUT.lerp(oldX, newX, Keyframe.getProgress(timeTicks, xLastChangedTicks, ANIMATION_SPEED));
            }
            if (y != newY) {
                y = Easing.OCT_EASE_OUT.lerp(oldY, newY, Keyframe.getProgress(timeTicks, yLastChangedTicks, ANIMATION_SPEED));
            }
        }

        private void render(GuiGraphics guiGraphics) {
            PoseStack stack = guiGraphics.pose();
            stack.pushPose();
            stack.translate(x, y, 0);
            notification.render(guiGraphics);
            stack.popPose();
        }
    }
}
