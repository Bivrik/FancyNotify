package net.bivrik.fancynotify.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;

import java.util.*;

public class Particle2DEngine {
    private static final int MAX = 512;
    private static final Random RANDOM = new Random();

    private final List<Particle2D> particles = new ArrayList<>();

    public void spawn(Particle2DSetup setup, int amount) {
        List<Particle2D> newParticles = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++) {
            newParticles.add(createParticle(setup));
        }
        addAll(newParticles);
    }

    private void addAll(List<Particle2D> newParticles) {
        synchronized (particles) {
            while (particles.size() + newParticles.size() > MAX) {
                particles.removeFirst();
            }

            particles.addAll(newParticles);
        }
    }

    private Particle2D createParticle(Particle2DSetup setup) {
        return new Particle2D(
                getRandomized(setup.lifetimeTicks, setup.spreadLifetimeTicks),
                getRandomized(setup.x, setup.spreadX),
                getRandomized(setup.y, setup.spreadY),
                getRandomized(setup.angle, setup.spreadAngle),
                getRandomized(setup.speed, setup.spreadSpeed),
                setup.movementFriction,
                getRandomized(setup.startRotation, setup.spreadStartRotation),
                getRandomized(setup.endRotation, setup.spreadEndRotation),
                getRandomized(setup.startScale, setup.spreadStartScale),
                getRandomized(setup.endScale, setup.spreadEndScale),
                setup.color);
    }

    private int getRandomized(int value, int range) {
        return value + RANDOM.nextInt(-range, range + 1);
    }

    private float getRandomized(float value, float range) {
        return value + RANDOM.nextFloat() * 2 * range - range;
    }

    public void tick() {
        if (particles.isEmpty()) {
            return;
        }

        synchronized (particles) {
            Iterator<Particle2D> iterator = particles.iterator();
            while (iterator.hasNext()) {
                Particle2D particle = iterator.next();
                particle.tick();
                if (particle.isDead()) {
                    iterator.remove();
                }
            }
        }
    }

    // Better do batching in the future, but it's fine for now
    public void render(GuiGraphics guiGraphics, float partialTick) {
        if (particles.isEmpty()) {
            return;
        }

        PoseStack stack = guiGraphics.pose();
        stack.pushPose();
        stack.translate(0, 0, 1200);
        synchronized (particles) {
            for (Particle2D particle : particles) {
                particle.render(guiGraphics, partialTick);
            }
        }
        stack.popPose();
    }
}
