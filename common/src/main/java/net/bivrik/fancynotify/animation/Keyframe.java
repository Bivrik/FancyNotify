package net.bivrik.fancynotify.animation;

import net.minecraft.util.Mth;

public class Keyframe {
    public static float getProgress(float timeTicks, float startPositionTicks, float durationTicks) {
        return Mth.clamp((timeTicks - startPositionTicks) / durationTicks, 0.0f, 1.0f);
    }

    public static boolean isActive(float progress) {
        return progress >= 0.0f && progress < 1.0f;
    }
}
