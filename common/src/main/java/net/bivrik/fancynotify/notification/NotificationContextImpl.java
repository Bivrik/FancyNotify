package net.bivrik.fancynotify.notification;

import net.bivrik.fancynotify.api.NotificationContext;

public record NotificationContextImpl(float globalX, float globalY, float timeTicks, int animationDuration) implements NotificationContext {
    @Override
    public float getGlobalX() {
        return globalX;
    }

    @Override
    public float getGlobalY() {
        return globalY;
    }

    @Override
    public float getTimeTicks() {
        return timeTicks;
    }

    @Override
    public int getAnimationDurationTicks() {
        return animationDuration;
    }
}
