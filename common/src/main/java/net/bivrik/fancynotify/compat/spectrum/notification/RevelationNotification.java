package net.bivrik.fancynotify.compat.spectrum.notification;

import net.bivrik.fancynotify.FancyNotify;
import net.bivrik.fancynotify.api.Notification;
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

public class RevelationNotification extends FancyNotification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/spectrum/message");
    private static final Component TITLE = Component.translatable("spectrum.toast.revelation.title");
    private static final Component MESSAGE = Component.translatable("spectrum.toast.revelation.text");
    private static final int TITLE_COLOR = new Color(115, 40, 244).getRGB();
    private static final int MESSAGE_COLOR = new Color(35, 35, 35).getRGB();

    private final ItemStack icon;
    private final SoundEvent sound;

    private boolean isSoundPlayed;

    public RevelationNotification(ItemStack icon, SoundEvent sound) {
        super(TITLE, MESSAGE);

        this.icon = icon;
        this.sound = sound;
    }

    @Override
    public boolean shouldDisplay() {
        return this.filters.isSpectrumRevelationNotificationEnabled.get();
    }

    @Override
    public void update(NotificationContext context) {
        if (!isSoundPlayed && context.getTimeTicks() > 0) {
            isSoundPlayed = true;
            this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(sound, 1.0f, 0.6f));
        }
    }

    @Override
    public void draw(NotificationGraphics graphics, float partialTick) {
        graphics.sprite(BACKGROUND, 0, 0, getWidth(), getHeight());
        graphics.text(getTitle(), getTextOffset(), 7, TITLE_COLOR);
        graphics.multiline(getWrappedMessage(), getTextOffset(), 18, MESSAGE_COLOR);
        graphics.unwrap().renderFakeItem(icon, 8, getCenterY() - 8);
    }
}
