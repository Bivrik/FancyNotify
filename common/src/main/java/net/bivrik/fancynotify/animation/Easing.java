package net.bivrik.fancynotify.animation;

public enum Easing {
    LINEAR(t -> t),
    SINE_IN(t -> (float) (1 - Math.cos(t * Math.PI * 0.5f))),
    SINE_OUT(t -> (float) Math.sin(t * Math.PI * 0.5f)),
    QUART_EASE_IN(t -> {
        float t2 = t * t;
        return t2 * t2;
    }),
    QUART_EASE_OUT(t -> {
        float x = 1 - t;
        float x2 = x * x;
        return 1 - (x2 * x2);
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

    public int lerp(int start, int end, float progress) {
        int delta = end - start;
        float easedProgress = mathEasing.apply(Math.clamp(progress, 0.0f, 1.0f));
        if (easedProgress + THRESHOLD >= 1.0f) {
            return end;
        }
        return (int) (start + delta * easedProgress);
    }

    @FunctionalInterface
    private interface MathEasing {
        float apply(float progress);
    }
}
