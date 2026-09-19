package net.bivrik.compat.spectrum.notification;

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

public class RevelationNotification extends Notification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/spectrum/message");
    private static final Component TITLE = Component.translatable("spectrum.toast.revelation.title");
    private static final Component MESSAGE = Component.translatable("spectrum.toast.revelation.text");
    private static final int TITLE_COLOR = new Color(115, 40, 244).getRGB();
    private static final int MESSAGE_COLOR = new Color(35, 35, 35).getRGB();

    private final ItemStack icon;
    private final SoundEvent sound;

    private boolean isSoundPlayed;

    public RevelationNotification(NotificationManager manager, ItemStack icon, SoundEvent sound) {
        super(manager, TITLE, MESSAGE);

        this.icon = icon;
        this.sound = sound;
    }

    @Override
    public boolean shouldDisplay() {
        return this.filtersConfig.isSpectrumRevelationNotificationEnabled.get();
    }

    @Override
    protected void onUpdate() {
        if (!isSoundPlayed && this.timeTicks >= 0) {
            isSoundPlayed = true;
            this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(this.sound, 1.0f, 0.6f));
        }
    }

    @Override
    protected void draw(GuiGraphics guiGraphics) {
        drawSprite(guiGraphics, BACKGROUND, 0, 0, getWidth(), getHeight());
        drawText(guiGraphics, getTitle(), getTextOffset(), 7, TITLE_COLOR);
        drawMessage(guiGraphics, getTextOffset(), 18, MESSAGE_COLOR);
        guiGraphics.renderFakeItem(icon, 8, getCenterY() - 8);
    }
}
