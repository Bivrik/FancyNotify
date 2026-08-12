package net.bivrik.fancynotify.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;

public class Particle2D {
    private boolean isAlive = true;
    private int timeTicks;
    private final int lifetimeTicks;

    private float previousX;
    private float previousY;
    private float currentX;
    private float currentY;
    private float velocityX;
    private float velocityY;
    private final float movementFriction;

    private float rotation;
    private final int startRotation;
    private final int endRotation;

    private float scale;
    private final float startScale;
    private final float endScale;

    public Particle2D(int lifetimeTicks, float spawnX, float spawnY, int angle, float speed, float movementFriction, int startRotation, int endRotation, float startScale, float endScale) {
        this.lifetimeTicks = Math.max(lifetimeTicks, 0);
        this.movementFriction = Math.clamp(1.0f - movementFriction, 0, 1);

        this.previousX = spawnX;
        this.previousY = spawnY;
        this.currentX = spawnX;
        this.currentY = spawnY;

        double angleInRadians = Math.toRadians(angle);
        velocityX = (float) (Math.cos(angleInRadians) * speed);
        velocityY = (float) (Math.sin(angleInRadians) * speed);

        rotation = startRotation;
        this.startRotation = startRotation;
        this.endRotation = endRotation;

        scale = startScale;
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

        float progress = Math.min((float) timeTicks / lifetimeTicks, 1);
        rotation = startRotation + (endRotation - startRotation) * progress;
        scale = startScale + (endScale - startScale) * progress;
    }

    public boolean isAlive() {
        return isAlive;
    }

    // Batching but it's too much effort for now
    public void render(GuiGraphics guiGraphics, float partialTick) {
        if (!isAlive) {
            return;
        }

        float x = previousX + (currentX - previousX) * partialTick;
        float y = previousY + (currentY - previousY) * partialTick;
        PoseStack stack = guiGraphics.pose();
        stack.pushPose();
        stack.translate(x + 2, y + 2, 0);
        stack.scale(scale, scale, 1);
        stack.translate(-2, -2, 0);
        stack.rotateAround(Axis.ZP.rotationDegrees(rotation), 2, 2, 0);
        guiGraphics.fill(0, 0, 4, 4, -1);
        stack.popPose();
    }
}
