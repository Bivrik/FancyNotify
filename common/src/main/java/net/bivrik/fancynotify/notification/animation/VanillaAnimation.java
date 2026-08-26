package net.bivrik.fancynotify.notification.animation;

import net.bivrik.fancynotify.animation.Easing;
import net.bivrik.fancynotify.animation.Keyframe;
import net.bivrik.fancynotify.config.GeneralConfig;
import net.bivrik.fancynotify.notification.NotificationAnimator;
import net.bivrik.fancynotify.notification.NotificationState;

public class VanillaAnimation extends NotificationAnimator {
    public VanillaAnimation(GeneralConfig generalConfig) {
        super(generalConfig);
    }

    @Override
    public void update(float timeTicks, NotificationState state, float animationTimingTicks, int width, int height, float animationDurationTicks) {
        switch (state) {
            case SHOWING -> {
                float startX = width + generalConfig.padding.get();
                float startAlpha = 0;

                float endX = 0;
                float endAlpha = 1;

                float showingProgress = Keyframe.getProgress(timeTicks, animationTimingTicks, animationDurationTicks);
                if (Keyframe.isActive(showingProgress)) {
                    x = Easing.SINE_OUT.lerp(startX, endX, showingProgress);
                    alpha = Easing.SINE_OUT.lerp(startAlpha, endAlpha, showingProgress);
                }

                if (timeTicks >= animationTimingTicks + animationDurationTicks) {
                    x = endX;
                    alpha = endAlpha;
                }
            }
            case VISIBLE -> {
                x = 0;
                alpha = 1;
            }
            case HIDING -> {
                float startX = 0;
                float startAlpha = 1;

                float endX = width + generalConfig.padding.get();
                float endAlpha = 0;

                float hidingProgress = Keyframe.getProgress(timeTicks, animationTimingTicks, animationDurationTicks);
                if (Keyframe.isActive(hidingProgress)) {
                    x = Easing.SINE_IN.lerp(startX, endX, hidingProgress);
                    alpha = Easing.SINE_IN.lerp(startAlpha, endAlpha, hidingProgress);
                }

                if (timeTicks >= animationTimingTicks + animationDurationTicks) {
                    x = endX;
                    alpha = endAlpha;
                }
            }
        }
    }
}
