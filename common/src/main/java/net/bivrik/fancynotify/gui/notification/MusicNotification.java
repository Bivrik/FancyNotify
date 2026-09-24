package net.bivrik.fancynotify.gui.notification;

import net.bivrik.fancynotify.FancyNotify;
import net.bivrik.fancynotify.api.Notification;
import net.bivrik.fancynotify.api.NotificationGraphics;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class MusicNotification extends FancyNotification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/music");
    private static final ResourceLocation ICON = ResourceLocations.of("icons/music");

    public MusicNotification(Component title, Component message) {
        super(title, message);
    }

    @Override
    public boolean shouldDisplay() {
        return this.filters.isMusicNotificationEnabled.get();
    }

    @Override
    public void draw(NotificationGraphics graphics, float partialTick) {
        graphics.sprite(BACKGROUND, 0, 0, getWidth(), getHeight());
        graphics.text(getTitle(), getTextOffset(), 7, Color.cyan.getRGB());
        graphics.multiline(getWrappedMessage(), getTextOffset(), 18, -1);
        graphics.sprite(ICON, 4, getCenterY() - 10, 21, 21);
    }
}
