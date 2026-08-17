package net.bivrik.fancynotify.notification.animation;

import net.bivrik.fancynotify.animation.Easing;
import net.bivrik.fancynotify.animation.Keyframe;
import net.bivrik.fancynotify.config.GeneralConfig;
import net.bivrik.fancynotify.notification.NotificationAnimator;
import net.bivrik.fancynotify.notification.NotificationState;

public class TopDownAnimation extends NotificationAnimator {
    public TopDownAnimation(GeneralConfig generalConfig) {
        super(generalConfig);
    }

    @Override
    public void update(float timeTicks, NotificationState state, float animationTimingTicks, int width, int height, float animationDurationTicks) {
        switch (state) {
            case SHOWING -> {
                float startY = -(height + generalConfig.padding.get());
                float startAlpha = 0;

                float endY = 0;
                float endAlpha = 1.0f;

                float showingProgress = Keyframe.getProgress(timeTicks, animationTimingTicks, animationDurationTicks);
                if (Keyframe.isActive(showingProgress)) {
                    y = Easing.QUART_EASE_OUT.lerp(startY, endY, showingProgress);
                    alpha = Easing.QUART_EASE_OUT.lerp(startAlpha, endAlpha, showingProgress);
                }

                if (timeTicks >= animationTimingTicks + animationDurationTicks) {
                    y = endY;
                    alpha = endAlpha;
                }
            }
            case HIDING -> {
                float startY = 0;
                float startAlpha = 1.0f;

                float endY = -(height + generalConfig.padding.get());
                float endAlpha = 0;

                float hidingProgress = Keyframe.getProgress(timeTicks, animationTimingTicks, animationDurationTicks);
                if (Keyframe.isActive(hidingProgress)) {
                    y = Easing.QUART_EASE_IN.lerp(startY, endY, hidingProgress);
                    alpha = Easing.QUART_EASE_IN.lerp(startAlpha, endAlpha, hidingProgress);
                }

                if (timeTicks >= animationTimingTicks + animationDurationTicks) {
                    y = endY;
                    alpha = endAlpha;
                }
            }
        }
    }
}
