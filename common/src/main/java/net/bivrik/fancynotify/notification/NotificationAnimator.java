package net.bivrik.fancynotify.notification;

import net.bivrik.fancynotify.config.GeneralConfig;

public abstract class NotificationAnimator {
    protected final GeneralConfig generalConfig;

    protected float x = 0;
    protected float y = 0;
    protected float scaleX = 1;
    protected float scaleY = 1;
    protected int rotation = 0;
    protected float alpha = 1;

    public NotificationAnimator(GeneralConfig generalConfig) {
        this.generalConfig = generalConfig;
    }

    public final float getX() {
        return x;
    }

    public final float getY() {
        return y;
    }

    public final float getScaleX() {
        return scaleX;
    }

    public final float getScaleY() {
        return scaleY;
    }

    public final int getRotation() {
        return rotation;
    }

    public final float getAlpha() {
        return alpha * generalConfig.notificationsTransparency.get();
    }

    public abstract void update(float timeTicks, NotificationState state, float animationTimingTicks, int width, int height, float animationDurationTicks);
}
