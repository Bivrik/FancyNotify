package net.bivrik.fancynotify;

public enum Easing {
    LINEAR(t -> t),
    SINE_IN(t -> (float) (1 - Math.cos(t * Math.PI * 0.5f))),
    SINE_OUT(t -> (float) Math.sin(t * Math.PI * 0.5f)),
    OCT_EASE_IN(t -> {
        float t2 = t * t;
        float t4 = t2 * t2;
        return t4 * t4;
    }),
    OCT_EASE_OUT(t -> {
        float x = 1 - t;
        float x2 = x * x;
        float x4 = x2 * x2;
        return 1 - (x4 * x4);
    });

    private static final float THRESHOLD = 0.004f;

    private final MathEasing mathEasing;

    Easing(MathEasing mathEasing) {
        this.mathEasing = mathEasing;
    }

    public float lerp(float start, float end, float progress) {
        float delta = end - start;
        float easedProgress = mathEasing.apply(Math.clamp(progress, 0.0f, 1.0f));
        if (easedProgress + THRESHOLD >= 1.0f) {
            return end;
        }
        return start + delta * easedProgress;
    }

    @FunctionalInterface
    private interface MathEasing {
        float apply(float progress);
    }
}
