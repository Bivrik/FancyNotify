package net.bivrik.fancynotify;

public class Keyframe {
    public static float getProgress(float timeTicks, int startPositionTicks, int durationTicks) {
        return Math.clamp((timeTicks - startPositionTicks) / durationTicks, 0.0f, 1.0f);
    }

    public static boolean isActive(float progress) {
        return progress > 0 && progress != 1;
    }
}
