package net.bivrik.fancynotify.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;

import java.awt.*;

public class Particle2D {
    private boolean isAlive = true;
    private int timeTicks;
    private final int lifetimeTicks;

    private final Color color;

    private float previousX;
    private float previousY;
    private float currentX;
    private float currentY;
    private float velocityX;
    private float velocityY;
    private final float movementFriction;

    private final int startRotation;
    private final int endRotation;

    private final float startScale;
    private final float endScale;

    public Particle2D(int lifetimeTicks, float spawnX, float spawnY, int angle, float speed, float movementFriction, int startRotation, int endRotation, float startScale, float endScale, Color color) {
        this.lifetimeTicks = Math.max(lifetimeTicks, 0);
        this.movementFriction = Math.clamp(1.0f - movementFriction, 0, 1);
        this.color = color;

        previousX = spawnX;
        previousY = spawnY;
        currentX = spawnX;
        currentY = spawnY;

        double angleInRadians = Math.toRadians(angle);
        velocityX = (float) (Math.cos(angleInRadians) * speed);
        velocityY = (float) (Math.sin(angleInRadians) * speed);

        this.startRotation = startRotation;
        this.endRotation = endRotation;

        this.startScale = startScale;
        this.endScale = endScale;
    }

    public void tick() {
        if (!isAlive) {
            return;
        }

        timeTicks++;
        if (timeTicks > lifetimeTicks) {
            isAlive = false;
            return;
        }

        previousX = currentX;
        previousY = currentY;

        velocityX *= movementFriction;
        velocityY *= movementFriction;

        currentX += velocityX;
        currentY += velocityY;
    }

    public boolean isDead() {
        return !isAlive;
    }

    // Batching but it's too much effort for now
    public void render(GuiGraphics guiGraphics, float partialTick) {
        if (!isAlive || timeTicks == 0) {
            return;
        }

        float renderTimeTicks = timeTicks + partialTick;
        float progress = Math.min(renderTimeTicks / lifetimeTicks, 1);

        float renderX = previousX + (currentX - previousX) * partialTick;
        float renderY = previousY + (currentY - previousY) * partialTick;
        float renderRotation = startRotation + (endRotation - startRotation) * progress;
        float renderScale = startScale + (endScale - startScale) * progress;

        PoseStack stack = guiGraphics.pose();
        stack.pushPose();
        stack.translate(renderX, renderY, 0);
        stack.scale(renderScale, renderScale, 1);
        stack.rotateAround(Axis.ZP.rotationDegrees(renderRotation), 0, 0, 0);
        guiGraphics.fill(-2, -2, 2, 2, color.getRGB());
        stack.popPose();
    }
}
