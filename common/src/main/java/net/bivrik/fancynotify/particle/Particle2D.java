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
    private float x;
    private float y;
    private float velocityX;
    private float velocityY;
    private final float movementFriction;

    private float previousRotation;
    private float rotation;
    private final int startRotation;
    private final int endRotation;

    private float previousScale;
    private float scale;
    private final float startScale;
    private final float endScale;

    public Particle2D(int lifetimeTicks, float spawnX, float spawnY, int angle, float speed, float movementFriction, int startRotation, int endRotation, float startScale, float endScale) {
        this.lifetimeTicks = Math.max(lifetimeTicks, 0);
        this.movementFriction = Math.clamp(1.0f - movementFriction, 0, 1);

        previousX = spawnX;
        previousY = spawnY;
        x = spawnX;
        y = spawnY;

        double angleInRadians = Math.toRadians(angle);
        velocityX = (float) (Math.cos(angleInRadians) * speed);
        velocityY = (float) (Math.sin(angleInRadians) * speed);

        previousRotation = startRotation;
        rotation = startRotation;
        this.startRotation = startRotation;
        this.endRotation = endRotation;

        previousScale = startScale;
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

        previousX = x;
        previousY = y;

        velocityX *= movementFriction;
        velocityY *= movementFriction;

        x += velocityX;
        y += velocityY;

        float progress = Math.min((float) timeTicks / lifetimeTicks, 1);

        previousRotation = rotation;
        rotation = startRotation + (endRotation - startRotation) * progress;

        previousScale = scale;
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

        float renderX = previousX + (x - previousX) * partialTick;
        float renderY = previousY + (y - previousY) * partialTick;
        float renderRotation = previousRotation + (rotation - previousRotation) * partialTick;
        float renderScale = previousScale + (scale - previousScale) * partialTick;

        PoseStack stack = guiGraphics.pose();
        stack.pushPose();
        stack.translate(renderX + 2, renderY + 2, 0);
        stack.scale(renderScale, renderScale, 1);
        stack.translate(-2, -2, 0);
        stack.rotateAround(Axis.ZP.rotationDegrees(renderRotation), 2, 2, 0);
        guiGraphics.fill(0, 0, 4, 4, -1);
        stack.popPose();
    }
}
