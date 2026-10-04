package net.bivrik.fancynotify.notification.animation;

import net.bivrik.fancynotify.notification.NotificationState;

public class VanillaAnimator extends NotificationAnimator {
    @Override
    public void update(float elapsedTicks, NotificationState state, float animationTimingTicks, int width, int height, float animationDurationTicks) {
        switch (state) {
            case SHOWING -> {
                float startX = width;
                float startAlpha = 0;

                float endX = 0;
                float endAlpha = 1;

                float showingProgress = Keyframe.getProgress(elapsedTicks, animationTimingTicks, animationDurationTicks);
                if (Keyframe.isActive(showingProgress)) {
                    x = Easing.SINE_OUT.lerp(startX, endX, showingProgress);
                    alpha = Easing.SINE_OUT.lerp(startAlpha, endAlpha, showingProgress);
                }

                if (elapsedTicks >= animationTimingTicks + animationDurationTicks) {
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

                float endX = width;
                float endAlpha = 0;

                float hidingProgress = Keyframe.getProgress(elapsedTicks, animationTimingTicks, animationDurationTicks);
                if (Keyframe.isActive(hidingProgress)) {
                    x = Easing.SINE_IN.lerp(startX, endX, hidingProgress);
                    alpha = Easing.SINE_IN.lerp(startAlpha, endAlpha, hidingProgress);
                }

                if (elapsedTicks >= animationTimingTicks + animationDurationTicks) {
                    x = endX;
                    alpha = endAlpha;
                }
            }
        }
    }
}
