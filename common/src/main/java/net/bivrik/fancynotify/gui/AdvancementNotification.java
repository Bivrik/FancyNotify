package net.bivrik.fancynotify.gui;

import net.bivrik.fancynotify.NotificationManager;
import net.bivrik.fancynotify.ResourceLocations;
import net.bivrik.fancynotify.core.Common;
import net.bivrik.fancynotify.particle.Particle2DSetup;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

public class AdvancementNotification extends Notification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/advancement");

    private final ItemStack icon;
    private final boolean isChallenge;
    private final int color;

    private boolean isSoundPlayed;

    public AdvancementNotification(NotificationManager manager, Component title, AdvancementType type, ItemStack icon) {
        super(manager, type.getDisplayName(), title);

        this.icon = icon;
        this.isChallenge = type == AdvancementType.CHALLENGE;
        this.color = this.isChallenge ? new Color(255, 119, 255).getRGB() : Color.yellow.getRGB();
    }

    @Override
    public boolean shouldDisplay() {
        return this.filtersConfig.isAdvancementNotificationEnabled.get();
    }

    @Override
    public int getLifeTimeTicks() {
        return super.getLifeTimeTicks() + 30;
    }

    @Override
    public void onShowing() {
        super.onShowing();

        Particle2DSetup setup = new Particle2DSetup.Builder(30, this.globalX + getWidth() / 2.0f, this.globalY + getHeight() / 2.0f)
                .angle(-180).spreadAngle(8)
                .spreadX(5).spreadY(10)
                .movementFriction(0.16f)
                .speed(0).spreadSpeed(16)
                .startRotation(-90).spreadStartRotation(90)
                .endRotation(90).spreadEndRotation(90)
                .build();
        Common.getParticle2DEngine().spawn(setup, 24);
    }

    @Override
    public void onUpdate() {
        int animationDuration = this.generalConfig.animationDuration.get();
        if (!isSoundPlayed && this.timeTicks >= animationDuration - animationDuration / 2f) {
            isSoundPlayed = true;
            if (isChallenge) {
                this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1, 1));
            }
        }
    }

    @Override
    public void draw(GuiGraphics guiGraphics) {
        drawSprite(guiGraphics, BACKGROUND, 0, 0, getWidth(), getHeight());
        drawText(guiGraphics, getTitle(), getTextOffset(), 7, color);
        drawMessage(guiGraphics, getTextOffset(), 18, -1);
        guiGraphics.renderFakeItem(icon, 8, getCenterY() - 8);
    }
}
