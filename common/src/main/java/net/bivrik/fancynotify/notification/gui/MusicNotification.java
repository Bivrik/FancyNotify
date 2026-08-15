package net.bivrik.fancynotify.notification.gui;

import net.bivrik.fancynotify.notification.Notification;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class MusicNotification extends Notification {
    private static final ResourceLocation ICON = ResourceLocations.of("textures/gui/icons/music.png");

    public MusicNotification(NotificationManager manager, Component title, Component message) {
        super(manager, title, message);
    }

    @Override
    public boolean shouldDisplay() {
        return this.filtersConfig.isMusicNotificationEnabled.get();
    }

    @Override
    public void draw(GuiGraphics guiGraphics) {
        drawBackground(guiGraphics, 128, 32);
        drawText(guiGraphics, getTitle(), getTextOffset(), 7, Color.cyan.getRGB());
        drawMessage(guiGraphics, getTextOffset(), 18, -1);
        long animationTiming = (System.currentTimeMillis() / 250 % 5) * 21;
        guiGraphics.blit(ICON,
                4, getCenterY() - 10,
                0, animationTiming,
                21, 21, 21, 105);
    }
}
