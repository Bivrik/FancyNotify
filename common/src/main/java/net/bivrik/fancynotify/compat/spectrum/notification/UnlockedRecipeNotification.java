package net.bivrik.fancynotify.compat.spectrum.notification;

import net.bivrik.fancynotify.api.NotificationContext;
import net.bivrik.fancynotify.api.NotificationGraphics;
import net.bivrik.fancynotify.gui.notification.FancyNotification;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.List;

public class UnlockedRecipeNotification extends FancyNotification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/spectrum/recipe");
    private static final int TITLE_COLOR = new Color(115, 40, 244).getRGB();
    private static final int MESSAGE_COLOR = new Color(35, 35, 35).getRGB();

    private final List<ItemStack> icons;
    private final SoundEvent sound;

    private boolean isSoundPlayed;

    public UnlockedRecipeNotification(Component title, Component message, List<ItemStack> icons, SoundEvent sound) {
        super(title, message);

        this.icons = icons;
        this.sound = sound;
    }

    @Override
    public boolean shouldDisplay() {
        return this.filters.isSpectrumUnlockedRecipeNotificationEnabled.get();
    }

    @Override
    public void update(NotificationContext context) {
        if (!isSoundPlayed && context.getTimeTicks() > 0) {
            isSoundPlayed = true;
            this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(sound, 1.0f, 0.6f));
        }
    }

    private float countTemp = 0;
    @Override
    public void render(NotificationGraphics graphics, float partialTick) {
        countTemp += 1 / 2f;
        graphics.sprite(BACKGROUND, 0, 0, getWidth(), getHeight());
        graphics.text(getTitle(), getTextOffset(), 7, TITLE_COLOR);
        graphics.multiline(getWrappedMessage(), getTextOffset(), 18, MESSAGE_COLOR);
        int orderedIndex = (int) (countTemp / Math.max(1f, (double) getLifeTimeTicks() / icons.size()) % icons.size());
        graphics.unwrap().renderFakeItem(icons.get(orderedIndex), 8, getCenterY() - 8);
    }
}
