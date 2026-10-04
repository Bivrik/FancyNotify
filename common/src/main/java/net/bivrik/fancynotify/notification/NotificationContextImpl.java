package net.bivrik.fancynotify.notification;

import net.bivrik.fancynotify.api.gui.notification.NotificationContext;

public record NotificationContextImpl(float globalX, float globalY, float elapsedTicks, int animationDurationTicks) implements NotificationContext {
    @Override
    public float getGlobalX() {
        return globalX;
    }

    @Override
    public float getGlobalY() {
        return globalY;
    }

    @Override
    public float getElapsedTicks() {
        return elapsedTicks;
    }

    @Override
    public int getAnimationDurationTicks() {
        return animationDurationTicks;
    }
}
