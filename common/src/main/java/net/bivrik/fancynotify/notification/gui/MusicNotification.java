package net.bivrik.fancynotify.notification.gui;

import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.bivrik.fancynotify.notification.Notification;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class MusicNotification extends Notification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/music");
    private static final ResourceLocation ICON = ResourceLocations.of("icons/music");

    public MusicNotification(NotificationManager manager, Component title, Component message) {
        super(manager, title, message);
    }

    @Override
    public boolean shouldDisplay() {
        return this.filtersConfig.isMusicNotificationEnabled.get();
    }

    @Override
    public void draw(GuiGraphics guiGraphics) {
        drawSprite(guiGraphics, BACKGROUND, 0, 0, getWidth(), getHeight());
        drawText(guiGraphics, getTitle(), getTextOffset(), 7, Color.cyan.getRGB());
        drawMessage(guiGraphics, getTextOffset(), 18, -1);
        drawSprite(guiGraphics, ICON, 4, getCenterY() - 10, 21, 21);
    }
}
