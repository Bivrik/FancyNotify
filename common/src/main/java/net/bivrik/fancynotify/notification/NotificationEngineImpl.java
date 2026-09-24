package net.bivrik.fancynotify.notification;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bivrik.fancynotify.api.ExpandableNotification;
import net.bivrik.fancynotify.api.Notification;
import net.bivrik.fancynotify.config.ConfigManager;
import net.bivrik.fancynotify.config.data.GeneralConfig;
import net.bivrik.fancynotify.core.Log;
import net.bivrik.fancynotify.notification.animation.Easing;
import net.bivrik.fancynotify.notification.animation.Keyframe;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class NotificationEngineImpl implements NotificationEngine {
    private final Minecraft minecraft;
    private final ConfigManager configManager;
    private final GeneralConfig config;
    private final DeltaTracker deltaTracker;

    private final List<NotificationEntry> allEntries = new ArrayList<>();
    private final Deque<NotificationEntry> entryQueue = new ArrayDeque<>();
    private final List<NotificationEntryHolder> showingHolders = new ArrayList<>();

    public NotificationEngineImpl(Minecraft minecraft, ConfigManager configManager) {
        this.minecraft = minecraft;
        this.configManager = configManager;
        this.config = configManager.getGeneralConfig();
        this.deltaTracker = minecraft.getTimer();
    }

    @Override
    public void add(Notification notification) {
        if (!notification.shouldDisplay()) {
            return;
        }
        String notificationClassName = notification.getClass().getSimpleName();

        for (NotificationEntry entry : allEntries) {
            if (entry.tryMerge(notification)) {
                Log.info("Expanded {}", notificationClassName);
                return;
            }
        }

        NotificationEntry entry = notification instanceof ExpandableNotification expandable
                ? new ExpandableNotificationEntry(expandable, minecraft, configManager)
                : new NotificationEntry(notification, minecraft, configManager);

        allEntries.add(entry);

        if (isShowingListFull()) {
            entryQueue.add(entry);
            Log.info("Added new {} to queue", notificationClassName);
        } else {
            showingHolders.add(computeEntryHolder(entry));
            Log.info("Showing new {}", notificationClassName);
        }
    }

    @Override
    public void clear() {
        allEntries.clear();
        entryQueue.clear();
        showingHolders.clear();
    }

    private boolean isShowingListFull() {
        return showingHolders.size() >= config.maxAmount.get();
    }

    @Override
    public boolean isEmpty() {
        return allEntries.isEmpty();
    }

    private void arrangeNotifications() {
        GeneralConfig.Anchor anchor = config.anchor.get();
        boolean isVertical = config.orientation.get() == GeneralConfig.Orientation.VERTICAL;
        int padding = config.padding.get();

        int x = 0;
        int y = 0;

        for (var holder : showingHolders) {
            int width = holder.getWidth();
            int height = holder.getHeight();

            int xOffset = anchor.isLeft() ? (isVertical ? 0 : -width) : (isVertical ? -width : 0);
            int yOffset = anchor.isTop() ? (isVertical ? -height : 0) : (isVertical ? 0 : -height);

            x += isVertical ? 0 : (anchor.isLeft() ? width : -width);
            y += isVertical ? (anchor.isTop() ? height : -height) : 0;

            holder.setX(x + xOffset);
            holder.setY(y + yOffset);

            if (isVertical) {
                y += anchor.isTop() ? padding : -padding;
            } else {
                x += anchor.isLeft() ? padding : -padding;
            }
        }
    }

    // Why is it so much conditions I need to change this BRO :sob:
    private NotificationEntryHolder computeEntryHolder(NotificationEntry entry) {
        GeneralConfig.Anchor anchor = config.anchor.get();
        boolean isVertical = config.orientation.get() == GeneralConfig.Orientation.VERTICAL;
        int padding = config.padding.get();

        int x = 0;
        int y = 0;

        for (var holder : showingHolders) {
            int width = holder.getWidth();
            int height = holder.getHeight();

            x += isVertical ? 0 : (anchor.isLeft() ? width : -width);
            y += isVertical ? (anchor.isTop() ? height : -height) : 0;

            if (isVertical) {
                y += anchor.isTop() ? padding : -padding;
            } else {
                x += anchor.isLeft() ? padding : -padding;
            }
        }

        int width = entry.getWidth();
        int height = entry.getHeight();

        int posX;
        int posY;

        if (isVertical) {
            posX = anchor.isLeft() ? 0 : -width;
            posY = anchor.isTop() ? y : y - height;
        } else {
            posX = anchor.isLeft() ? x : x - width;
            posY = anchor.isTop() ? 0 : -height;
        }

        return new NotificationEntryHolder(entry, posX, posY);
    }

    @Override
    public void update() {
        if (!showingHolders.isEmpty()) {
            float deltaTicks = deltaTracker.getGameTimeDeltaTicks();
            for (var iterator = showingHolders.iterator(); iterator.hasNext();) {
                var nextHolder = iterator.next();

                NotificationEntry entry = nextHolder.getNotificationEntry();
                if (entry.shouldRemove()) {
                    iterator.remove();
                    allEntries.remove(entry);
                    Log.info("Removed {}", entry.getContent().getClass().getSimpleName());
                    continue;
                }

                GeneralConfig.Anchor anchor = config.anchor.get();
                int padding = config.padding.get();
                float anchorX = anchor.isLeft() ? padding : minecraft.getWindow().getGuiScaledWidth() - padding;
                float anchorY = anchor.isTop() ? padding : minecraft.getWindow().getGuiScaledHeight() - padding;
                nextHolder.update(deltaTicks, anchorX, anchorY);
            }

            arrangeNotifications();
        }

        while (!entryQueue.isEmpty() && !isShowingListFull()) {
            NotificationEntry nextEntry = entryQueue.pollFirst();
            if (nextEntry != null) {
                showingHolders.add(computeEntryHolder(nextEntry));
                Log.info("Showing next " + nextEntry.getClass().getSimpleName());
            }
        }
    }

    @Override
    public void remove(Class<? extends Notification> notificationClass, Object id) {
        for (NotificationEntry entry : allEntries) {
            if (entry.getContent().getClass() != notificationClass || id.equals(entry.getId())) {
                continue;
            }

            if (entryQueue.remove(entry)) {
                allEntries.remove(entry);
            } else {
                entry.forceHide();
            }

            return;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics) {
        if (showingHolders.isEmpty() || minecraft.options.hideGui) {
            return;
        }

        PoseStack stack = guiGraphics.pose();
        stack.pushPose();

        if (config.debug.get()) {
            debugOverlay(guiGraphics, stack);
        } else {
            GeneralConfig.Anchor anchor = config.anchor.get();
            int padding = config.padding.get();
            int anchorX = anchor.isLeft() ? padding : guiGraphics.guiWidth() - padding;
            int anchorY = anchor.isTop() ? padding : guiGraphics.guiHeight() - padding;
            stack.translate(anchorX, anchorY, 800);
        }

        float partialTick = deltaTracker.getGameTimeDeltaPartialTick(false);
        for (var holder : showingHolders) {
            holder.render(guiGraphics, partialTick);
        }

        stack.popPose();
    }

    private void debugOverlay(GuiGraphics guiGraphics, PoseStack stack) {
        stack.translate(guiGraphics.guiWidth() / 2.0, guiGraphics.guiHeight() / 2.0, 800);

        guiGraphics.fill(-500, 0, 500, 1, -58254424);
        guiGraphics.fill(0, -500, 1, 500, -58254424);

        guiGraphics.fill(-500, -1, 500, 0, -812254424);
        guiGraphics.fill(-1, -500, 0, 500, -812254424);

        guiGraphics.drawString(minecraft.font, "(-1, 1)", -37, 6, -1);
        guiGraphics.drawString(minecraft.font, "(1, -1)", 6, -13, -1);
        guiGraphics.drawString(minecraft.font, "(0, 0)", -13, -3, -1);
    }

    private final static class NotificationEntryHolder {
        private final static int ANIMATION_SPEED = 20;

        private final NotificationEntry entry;

        private float x;
        private float oldX;
        private float newX;
        private float xLastChangedTicks;

        private float y;
        private float oldY;
        private float newY;
        private float yLastChangedTicks;

        private float timeTicks;

        public NotificationEntryHolder(NotificationEntry entry, float x, float y) {
            this.entry = entry;

            this.x = x;
            this.newX = x;
            this.y = y;
            this.newY = y;
        }

        public NotificationEntry getNotificationEntry() {
            return entry;
        }

        public int getWidth() {
            return entry.getWidth();
        }

        public int getHeight() {
            return entry.getHeight();
        }

        public void setX(float x) {
            if (x != newX) {
                oldX = this.x;
                newX = x;
                xLastChangedTicks = timeTicks;
            }
        }

        public void setY(float y) {
            if (y != newY) {
                oldY = this.y;
                newY = y;
                yLastChangedTicks = timeTicks;
            }
        }

        public void update(float deltaTicks, float anchorX, float anchorY) {
            timeTicks += deltaTicks;

            if (x != newX) {
                x = Easing.QUART_EASE_OUT.lerp(oldX, newX, Keyframe.getProgress(timeTicks, xLastChangedTicks, ANIMATION_SPEED));
            }
            if (y != newY) {
                y = Easing.QUART_EASE_OUT.lerp(oldY, newY, Keyframe.getProgress(timeTicks, yLastChangedTicks, ANIMATION_SPEED));
            }

            entry.update(deltaTicks, anchorX + x, anchorY + y);
        }

        public void render(GuiGraphics guiGraphics, float partialTick) {
            PoseStack stack = guiGraphics.pose();
            stack.pushPose();
            stack.translate(x, y, 0);
            entry.render(guiGraphics, partialTick);
            stack.popPose();
        }
    }
}
