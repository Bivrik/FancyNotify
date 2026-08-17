package net.bivrik.fancynotify.notification.gui;

import net.bivrik.fancynotify.notification.Notification;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.utility.Identifiers;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.awt.*;

public class MusicNotification extends Notification {
    private static final Identifier BACKGROUND = Identifiers.of("notifications/music");
    private static final Identifier ICON = Identifiers.of("icons/music");

    public MusicNotification(NotificationManager manager, Component title, Component message) {
        super(manager, title, message);
    }

    @Override
    public boolean shouldDisplay() {
        return this.filtersConfig.isMusicNotificationEnabled.get();
    }

    @Override
    public void draw(GuiGraphicsExtractor graphics) {
        drawSprite(graphics, BACKGROUND, 0, 0, getWidth(), getHeight());
        drawText(graphics, getTitle(), getTextOffset(), 7, Color.cyan.getRGB());
        drawMessage(graphics, getTextOffset(), 18, -1);
        drawSprite(graphics, ICON, 4, getCenterY() - 10, 21, 21);
    }
}
