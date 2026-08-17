package net.bivrik.fancynotify.particle;

import net.bivrik.fancynotify.config.ConfigManager;
import net.bivrik.fancynotify.config.GeneralConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.joml.Matrix3x2fStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Particle2DEngine {
    private static final int MAX = 512;
    private static final Random RANDOM = new Random();

    private final List<Particle2D> particles = new ArrayList<>();
    private final Minecraft minecraft;
    private final GeneralConfig generalConfig;

    public Particle2DEngine(Minecraft minecraft, ConfigManager configManager) {
        this.minecraft = minecraft;
        this.generalConfig = configManager.getGeneralConfig();
    }

    public void spawn(Particle2DSetup setup, int amount) {
        if (!generalConfig.particlesEnabled.get()) {
            return;
        }

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
    public void render(GuiGraphicsExtractor guiGraphicsExtractor, float partialTick) {
        if (particles.isEmpty() || minecraft.gui.hud.isHidden()) {
            return;
        }

        Matrix3x2fStack stack = guiGraphicsExtractor.pose();
        stack.pushMatrix();
        stack.translate(0, 0);
        synchronized (particles) {
            for (Particle2D particle : particles) {
                particle.render(guiGraphicsExtractor, partialTick);
            }
        }
        stack.popMatrix();
    }
}
