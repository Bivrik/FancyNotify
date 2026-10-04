package net.bivrik.fancynotify.notification.animation;

import net.bivrik.fancynotify.notification.NotificationState;

public class TopDownAnimator extends NotificationAnimator {
    @Override
    public void update(float elapsedTicks, NotificationState state, float animationTimingTicks, int width, int height, float animationDurationTicks) {
        switch (state) {
            case SHOWING -> {
                float startY = -height;
                float startAlpha = 0;

                float endY = 0;
                float endAlpha = 1;

                float showingProgress = Keyframe.getProgress(elapsedTicks, animationTimingTicks, animationDurationTicks);
                if (Keyframe.isActive(showingProgress)) {
                    y = Easing.QUART_EASE_OUT.lerp(startY, endY, showingProgress);
                    alpha = Easing.QUART_EASE_OUT.lerp(startAlpha, endAlpha, showingProgress);
                }

                if (elapsedTicks >= animationTimingTicks + animationDurationTicks) {
                    y = endY;
                    alpha = endAlpha;
                }
            }
            case VISIBLE -> {
                y = 0;
                alpha = 1;
            }
            case HIDING -> {
                float startY = 0;
                float startAlpha = 1;

                float endY = -height;
                float endAlpha = 0;

                float hidingProgress = Keyframe.getProgress(elapsedTicks, animationTimingTicks, animationDurationTicks);
                if (Keyframe.isActive(hidingProgress)) {
                    y = Easing.QUART_EASE_IN.lerp(startY, endY, hidingProgress);
                    alpha = Easing.QUART_EASE_IN.lerp(startAlpha, endAlpha, hidingProgress);
                }

                if (elapsedTicks >= animationTimingTicks + animationDurationTicks) {
                    y = endY;
                    alpha = endAlpha;
                }
            }
        }
    }
}
