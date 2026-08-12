package net.bivrik.fancynotify.particle;

public class Particle2DSetup {
    public final int lifetimeTicks;
    public final int spreadLifetimeTicks;
    public final float x;
    public final float spreadX;
    public final float y;
    public final float spreadY;
    public final int angle;
    public final int spreadAngle;
    public final float speed;
    public final float spreadSpeed;
    public final float movementFriction;
    public final int startRotation;
    public final int spreadStartRotation;
    public final int endRotation;
    public final int spreadEndRotation;
    public final float startScale;
    public final float spreadStartScale;
    public final float endScale;
    public final float spreadEndScale;

    private Particle2DSetup(Builder builder) {
        this.lifetimeTicks = builder.lifetimeTicks;
        this.spreadLifetimeTicks = builder.spreadLifetimeTicks;
        this.x = builder.x;
        this.spreadX = builder.spreadX;
        this.y = builder.y;
        this.spreadY = builder.spreadY;
        this.angle = builder.angle;
        this.spreadAngle = builder.spreadAngle;
        this.speed = builder.speed;
        this.spreadSpeed = builder.spreadSpeed;
        this.movementFriction = builder.movementFriction;
        this.startRotation = builder.startRotation;
        this.spreadStartRotation = builder.spreadStartRotation;
        this.endRotation = builder.endRotation;
        this.spreadEndRotation = builder.spreadEndRotation;
        this.startScale = builder.startScale;
        this.spreadStartScale = builder.spreadStartScale;
        this.endScale = builder.endScale;
        this.spreadEndScale = builder.spreadEndScale;
    }

    public static class Builder {
        private final int lifetimeTicks;
        private int spreadLifetimeTicks = 10;
        private final float x;
        private float spreadX = 10;
        private final float y;
        private float spreadY = 2;
        private int angle = -90;
        private int spreadAngle = 45;
        private float speed = 0.8f;
        private float spreadSpeed = 0.15f;
        private float movementFriction = 0;
        private int startRotation = 0;
        private int spreadStartRotation = 0;
        private int endRotation = 0;
        private int spreadEndRotation = 0;
        private float startScale = 1;
        private float spreadStartScale = 0.25f;
        private float endScale = 0;
        private float spreadEndScale = 0;

        public Builder(int lifetimeTicks, float x, float y) {
            this.lifetimeTicks = lifetimeTicks;
            this.x = x;
            this.y = y;
        }

        public Builder spreadLifetimeTicks(int spreadLifetimeTicks) {
            this.spreadLifetimeTicks = spreadLifetimeTicks;
            return this;
        }

        public Builder spreadX(float spreadX) {
            this.spreadX = spreadX;
            return this;
        }

        public Builder spreadY(float spreadY) {
            this.spreadY = spreadY;
            return this;
        }

        public Builder angle(int angle) {
            this.angle = angle;
            return this;
        }

        public Builder spreadAngle(int spreadAngle) {
            this.spreadAngle = spreadAngle;
            return this;
        }

        public Builder speed(float speed) {
            this.speed = speed;
            return this;
        }

        public Builder spreadSpeed(float spreadSpeed) {
            this.spreadSpeed = spreadSpeed;
            return this;
        }

        public Builder movementFriction(float movementFriction) {
            this.movementFriction = movementFriction;
            return this;
        }

        public Builder startRotation(int startRotation) {
            this.startRotation = startRotation;
            return this;
        }

        public Builder spreadStartRotation(int spreadStartRotation) {
            this.spreadStartRotation = spreadStartRotation;
            return this;
        }

        public Builder endRotation(int endRotation) {
            this.endRotation = endRotation;
            return this;
        }

        public Builder spreadEndRotation(int spreadEndRotation) {
            this.spreadEndRotation = spreadEndRotation;
            return this;
        }

        public Builder startScale(float startScale) {
            this.startScale = startScale;
            return this;
        }

        public Builder spreadStartScale(float spreadStartScale) {
            this.spreadStartScale = spreadStartScale;
            return this;
        }

        public Builder endScale(float endScale) {
            this.endScale = endScale;
            return this;
        }

        public Builder spreadEndScale(float spreadEndScale) {
            this.spreadEndScale = spreadEndScale;
            return this;
        }

        public Particle2DSetup build() {
            return new Particle2DSetup(this);
        }
    }
}
