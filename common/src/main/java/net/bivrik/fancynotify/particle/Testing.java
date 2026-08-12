package net.bivrik.fancynotify.particle;

import net.bivrik.fancynotify.core.FancyNotify;

// For testing
public class Testing {
    public void test(int width, int height) {
        Particle2DSetup setup = new Particle2DSetup.Builder(40, width / 2.0f, height / 2.0f)
                .angle(-180).spreadAngle(10)
                .spreadX(5).spreadY(10)
                .movementFriction(0.03f)
                .speed(2.5f).spreadSpeed(2)
                .startRotation(-90).spreadStartRotation(90)
                .endRotation(90).spreadEndRotation(90)
                .startScale(1.1f)
                .build();
        FancyNotify.getInstance().getParticleEngine().spawn(setup, 1);
    }
}
