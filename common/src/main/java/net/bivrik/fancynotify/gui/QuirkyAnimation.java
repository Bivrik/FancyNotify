package net.bivrik.fancynotify.gui;

import net.bivrik.fancynotify.Easing;
import net.bivrik.fancynotify.Keyframe;
import net.bivrik.fancynotify.config.GeneralConfig;

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
                    scaleX = Easing.QUART_EASE_OUT.lerp(0, 1, showingProgress);
                    scaleY = Easing.QUART_EASE_OUT.lerp(2, 1, showingProgress);
                    rotation = Easing.QUART_EASE_OUT.lerp(-0.15f, 0, showingProgress);
                }

                if (timeTicks >= animationTimingTicks + animationDurationTicks) {
                    scaleX = 1;
                    scaleY = 1;
                    rotation = 0;
                }
            }
            case HIDING -> {
                float hidingProgress = Keyframe.getProgress(timeTicks, animationTimingTicks, animationDurationTicks);
                if (Keyframe.isActive(hidingProgress)) {
                    scaleX = Easing.QUART_EASE_IN.lerp(1, 1.5f, hidingProgress);
                    scaleY = Easing.QUART_EASE_IN.lerp(1, 0, hidingProgress);
                    rotation = Easing.QUART_EASE_IN.lerp(0, 0.15f, hidingProgress);
                }

                if (timeTicks >= animationTimingTicks + animationDurationTicks) {
                    scaleX = 0;
                    scaleY = 0;
                    rotation = 0.15f;
                }
            }
        }
    }
}
