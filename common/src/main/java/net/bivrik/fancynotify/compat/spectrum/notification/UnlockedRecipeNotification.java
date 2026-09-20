package net.bivrik.fancynotify.compat.spectrum.notification;

import net.bivrik.fancynotify.notification.Notification;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.List;

public class UnlockedRecipeNotification extends Notification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/spectrum/recipe");
    private static final int TITLE_COLOR = new Color(115, 40, 244).getRGB();
    private static final int MESSAGE_COLOR = new Color(35, 35, 35).getRGB();

    private final List<ItemStack> icons;
    private final SoundEvent sound;

    private boolean isSoundPlayed;

    public UnlockedRecipeNotification(NotificationManager manager, Component title, Component message, List<ItemStack> icons, SoundEvent sound) {
        super(manager, title, message);

        this.icons = icons;
        this.sound = sound;
    }

    @Override
    public boolean shouldDisplay() {
        return this.filtersConfig.isSpectrumUnlockedRecipeNotificationEnabled.get();
    }

    @Override
    protected void onUpdate() {
        if (!isSoundPlayed && this.timeTicks >= 0) {
            isSoundPlayed = true;
            this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(this.sound, 1.0f, 1.0f));
        }
    }

    private float countTemp = 0;
    @Override
    protected void draw(GuiGraphics guiGraphics) {
        countTemp += 1 / 2f;
        drawSprite(guiGraphics, BACKGROUND, 0, 0, getWidth(), getHeight());
        drawText(guiGraphics, getTitle(), getTextOffset(), 7, TITLE_COLOR);
        drawMessage(guiGraphics, getTextOffset(), 18, MESSAGE_COLOR);
        int orderedIndex = (int) (countTemp / Math.max(1f, (double) getLifeTimeTicks() / icons.size()) % icons.size());
        guiGraphics.renderFakeItem(icons.get(orderedIndex), 8, getCenterY() - 8);
    }
}
