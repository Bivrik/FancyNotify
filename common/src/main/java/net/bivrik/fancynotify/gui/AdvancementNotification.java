package net.bivrik.fancynotify.gui;

import net.bivrik.fancynotify.NotificationManager;
import net.bivrik.fancynotify.ResourceLocations;
import net.bivrik.fancynotify.config.ConfigManager;
import net.bivrik.fancynotify.core.Common;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
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
    public void onUpdate() {
        if (!isSoundPlayed && this.timeTicks >= getAnimationDurationTicks() - getAnimationDurationTicks() / 2f) {
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
