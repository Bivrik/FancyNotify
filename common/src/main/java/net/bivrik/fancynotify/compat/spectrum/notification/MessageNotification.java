package net.bivrik.fancynotify.compat.spectrum.notification;

import net.bivrik.fancynotify.api.gui.icon.ItemIcon;
import net.bivrik.fancynotify.api.gui.notification.NotificationContext;
import net.bivrik.fancynotify.api.gui.notification.NotificationGraphics;
import net.bivrik.fancynotify.gui.notification.FancyNotification;
import net.bivrik.fancynotify.utility.Color;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;

public class MessageNotification extends FancyNotification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/spectrum/message");
    private static final int TITLE_COLOR = Color.create(115, 40, 244);
    private static final int MESSAGE_COLOR = Color.create(35, 35, 35);

    private final SoundEvent sound;

    private boolean isSoundPlayed;

    public MessageNotification(Component title, Component message, ItemStack icon, SoundEvent sound) {
        super(title, message);
        setIcon(new ItemIcon(icon));

        this.sound = sound;
    }

    @Override
    public boolean shouldDisplay() {
        return this.filters.isSpectrumMessageNotificationEnabled.get();
    }

    @Override
    public void onUpdate(NotificationContext context) {
        if (!isSoundPlayed && context.getElapsedTicks() > 0) {
            isSoundPlayed = true;
            this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(sound, 1.0f, 0.75f));
        }
    }

    @Override
    public void render(NotificationGraphics graphics) {
        graphics.sprite(BACKGROUND, 0, 0, getWidth(), getHeight());
        graphics.text(getTitle(), getTextOffset(), 7, TITLE_COLOR);
        graphics.multilineText(getWrappedMessage(), getTextOffset(), 18, MESSAGE_COLOR);
        graphics.icon(getIcon(), 8, getCenterY() - 8);
    }
}
