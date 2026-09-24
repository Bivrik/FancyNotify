package net.bivrik.fancynotify.notification;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bivrik.fancynotify.api.ExpandableNotification;
import net.bivrik.fancynotify.api.Notification;
import net.bivrik.fancynotify.config.ConfigManager;
import net.bivrik.fancynotify.config.data.GeneralConfig;
import net.bivrik.fancynotify.core.Log;
import net.bivrik.fancynotify.notification.animation.Easing;
import net.bivrik.fancynotify.notification.animation.Keyframe;
import net.bivrik.fancynotify.particle.Particle2DEngine;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class NotificationEngineImpl implements NotificationEngine {
    private final Minecraft minecraft;
    private final ConfigManager configManager;
    private final GeneralConfig config;
    private final DeltaTracker deltaTracker;

    private final List<NotificationHost> allNotificationHosts = new ArrayList<>();
    private final Deque<NotificationHost> notificationHostQueue = new ConcurrentLinkedDeque<>();
    private final List<NotificationHolder> currentNotificationHosts = new ArrayList<>();

    public NotificationEngineImpl(Minecraft minecraft, ConfigManager configManager) {
        this.minecraft = minecraft;
        this.configManager = configManager;
        this.config = configManager.getGeneralConfig();
        this.deltaTracker = minecraft.getTimer();
    }

    @Override
    public void add(Notification context) {
        if (!context.shouldDisplay()) {
            return;
        }

        for (NotificationHost host : allNotificationHosts) {
            if (host.tryMerge(context)) {
                Log.info("Expanded " + context.getClass().getSimpleName());
                return;
            }
        }

        NotificationHost host = context instanceof ExpandableNotification expandable
                ? new ExpandableNotificationHost(expandable, minecraft, configManager)
                : new NotificationHost(context, minecraft, configManager);

        if (hasCurrentSlots()) {
            Position position = computePosition(host);
            currentNotificationHosts.add(new NotificationHolder(host, position.x(), position.y()));
            Log.info("Showing new " + context.getClass().getSimpleName());
        } else {
            notificationHostQueue.add(host);
            Log.info("Added new " + context.getClass().getSimpleName() + " to queue");
        }
        allNotificationHosts.add(host);
    }

    @Override
    public void clear() {
        notificationHostQueue.clear();
        currentNotificationHosts.clear();
        allNotificationHosts.clear();
    }

    private boolean hasCurrentSlots() {
        return currentNotificationHosts.size() < config.maxAmount.get();
    }

    @Override
    public boolean isEmpty() {
        return currentNotificationHosts.isEmpty();
    }

    private void arrangeNotifications() {
        GeneralConfig.Anchor anchor = config.anchor.get();
        boolean isVertical = config.orientation.get() == GeneralConfig.Orientation.VERTICAL;
        int x = 0;
        int y = 0;
        for (var h : currentNotificationHosts) {
            int width = h.getWidth();
            int height = h.getHeight();

            int xOffset = anchor.isLeft() ? (isVertical ? 0 : -width) : (isVertical ? -width : 0);
            int yOffset = anchor.isTop() ? (isVertical ? -height : 0) : (isVertical ? 0 : -height);

            x += isVertical ? 0 : (anchor.isLeft() ? width : -width);
            y += isVertical ? (anchor.isTop() ? height : -height) : 0;

            h.setX(x + xOffset);
            h.setY(y + yOffset);

            int padding = config.padding.get();
            if (isVertical) {
                y += anchor.isTop() ? padding : -padding;
            } else {
                x += anchor.isLeft() ? padding : -padding;
            }
        }
    }

    // Why is it so much conditions I need to change this BRO :sob:
    private Position computePosition(NotificationHost notification) {
        GeneralConfig.Anchor anchor = config.anchor.get();
        boolean isVertical = config.orientation.get() == GeneralConfig.Orientation.VERTICAL;
        int padding = config.padding.get();

        int x = 0;
        int y = 0;
        for (var h : currentNotificationHosts) {
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

    @Override
    public void update() {
        if (!isEmpty()) {
            float deltaTicks = deltaTracker.getGameTimeDeltaTicks();
            for (var iterator = currentNotificationHosts.iterator(); iterator.hasNext();) {
                var notificationHolder = iterator.next();

                var notification = notificationHolder.getNotification();
                if (notification.shouldRemove()) {
                    iterator.remove();
                    Log.info("Removed {}", notification.content.getClass().getSimpleName());
                    allNotificationHosts.remove(notification);
                    continue;
                }

                GeneralConfig.Anchor anchor = config.anchor.get();
                int padding = config.padding.get();
                float anchorX = anchor.isLeft() ? padding : minecraft.getWindow().getGuiScaledWidth() - padding;
                float anchorY = anchor.isTop() ? padding : minecraft.getWindow().getGuiScaledHeight() - padding;
                notificationHolder.update(deltaTicks, anchorX, anchorY);
            }

            arrangeNotifications();
        }

        while (!notificationHostQueue.isEmpty() && hasCurrentSlots()) {
            NotificationHost next = notificationHostQueue.pollFirst();
            if (next != null) {
                Position position = computePosition(next);
                currentNotificationHosts.add(new NotificationHolder(next, position.x(), position.y()));
                Log.info("Showing next " + next.getClass().getSimpleName());
            }
        }
    }

    @Override
    public <T extends Notification> void remove(Class<T> notificationClass, Object id) {
        for (NotificationHost host : allNotificationHosts) {
            if (host.content.getClass() == notificationClass && id.equals(host.getId())) {
                if (notificationHostQueue.contains(host)) {
                    notificationHostQueue.remove(host);
                    allNotificationHosts.remove(host);
                } else {
                    host.forceHide();
                }
                return;
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, float partialTick) {
        if (currentNotificationHosts.isEmpty() || minecraft.options.hideGui) return;

        PoseStack stack = guiGraphics.pose();
        stack.pushPose();
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

        for (var notificationHolder : currentNotificationHosts) {
            notificationHolder.render(guiGraphics, partialTick);
        }

        stack.popPose();
    }

    private static class NotificationHolder {
        private final static int ANIMATION_SPEED = 20;

        private final NotificationHost notification;

        private float timeTicks;

        private float x;
        private float oldX;
        private float newX;
        private float xLastChangedTicks;

        private float y;
        private float oldY;
        private float newY;
        private float yLastChangedTicks;

        private NotificationHolder(NotificationHost notification, float x, float y) {
            this.notification = notification;

            this.x = x;
            this.newX = x;
            this.y = y;
            this.newY = y;
        }

        private NotificationHost getNotification() {
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
            }
        }

        private void update(float deltaTicks, float anchorX, float anchorY) {
            notification.update(deltaTicks, anchorX + x, anchorY + y);
            timeTicks += deltaTicks;

            if (x != newX) {
                x = Easing.QUART_EASE_OUT.lerp(oldX, newX, Keyframe.getProgress(timeTicks, xLastChangedTicks, ANIMATION_SPEED));
            }
            if (y != newY) {
                y = Easing.QUART_EASE_OUT.lerp(oldY, newY, Keyframe.getProgress(timeTicks, yLastChangedTicks, ANIMATION_SPEED));
            }
        }

        private void render(GuiGraphics guiGraphics, float partialTick) {
            PoseStack stack = guiGraphics.pose();
            stack.pushPose();
            stack.translate(x, y, 0);
            notification.render(guiGraphics, partialTick);
            stack.popPose();
        }
    }
}
