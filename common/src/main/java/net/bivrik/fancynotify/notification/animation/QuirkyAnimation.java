package net.bivrik.fancynotify.notification.animation;

import net.bivrik.fancynotify.animation.Easing;
import net.bivrik.fancynotify.animation.Keyframe;
import net.bivrik.fancynotify.config.GeneralConfig;
import net.bivrik.fancynotify.notification.NotificationAnimator;
import net.bivrik.fancynotify.notification.NotificationState;

public class QuirkyAnimation extends NotificationAnimator {
    public QuirkyAnimation(GeneralConfig generalConfig) {
        super(generalConfig);
    }

    @Override
    public void update(float timeTicks, NotificationState state, float animationTimingTicks, int width, int height, float animationDurationTicks) {
        switch (state) {
            case SHOWING -> {
                float showingProgress = Keyframe.getProgress(timeTicks, animationTimingTicks, animationDurationTicks);
                if (Keyframe.isActive(showingProgress)) {
                    scaleX = Easing.QUART_EASE_OUT.lerp(0, 1.0f, showingProgress);
                    scaleY = Easing.QUART_EASE_OUT.lerp(2.0f, 1.0f, showingProgress);
                    rotation = Easing.QUART_EASE_OUT.lerp(-18, 0, showingProgress);
                }

                if (timeTicks >= animationTimingTicks + animationDurationTicks) {
                    scaleX = 1;
                    scaleY = 1;
                    rotation = 0;
                }
            }
            case VISIBLE -> {
                scaleX = 1;
                scaleY = 1;
                rotation = 0;
            }
            case HIDING -> {
                float hidingProgress = Keyframe.getProgress(timeTicks, animationTimingTicks, animationDurationTicks);
                if (Keyframe.isActive(hidingProgress)) {
                    scaleX = Easing.QUART_EASE_IN.lerp(1.0f, 1.5f, hidingProgress);
                    scaleY = Easing.QUART_EASE_IN.lerp(1.0f, 0, hidingProgress);
                    rotation = Easing.QUART_EASE_IN.lerp(0, 18, hidingProgress);
                }

                if (timeTicks >= animationTimingTicks + animationDurationTicks) {
                    scaleX = 0;
                    scaleY = 0;
                    rotation = 18;
                }
            }
        }
    }
}
