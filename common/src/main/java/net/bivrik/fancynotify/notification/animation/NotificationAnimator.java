package net.bivrik.fancynotify.notification.animation;

import net.bivrik.fancynotify.notification.NotificationState;

public abstract class NotificationAnimator {
    protected float x = 0;
    protected float y = 0;
    protected float scaleX = 1.0f;
    protected float scaleY = 1.0f;
    protected float rotation = 0;
    protected float alpha = 1.0f;

    protected NotificationAnimator() {}

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

    public final float getRotation() {
        return rotation;
    }

    public final float getAlpha() {
        return alpha;
    }

    public abstract void update(float elapsedTicks, NotificationState state, float animationTimingTicks, int width, int height, float animationDurationTicks);
}
