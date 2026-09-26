package net.bivrik.fancynotify.gui.notification;

import net.bivrik.fancynotify.api.gui.icon.SpriteIcon;
import net.bivrik.fancynotify.api.gui.notification.NotificationGraphics;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class MusicNotification extends FancyNotification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/music");
    private static final ResourceLocation ICON = ResourceLocations.of("icons/music");
    private static final int TITLE_COLOR = Color.cyan.getRGB();

    public MusicNotification(Component title, Component message) {
        super(title, message);
        setIcon(new SpriteIcon(ICON, 21, 21));
    }

    @Override
    public boolean shouldDisplay() {
        return this.filters.isMusicNotificationEnabled.get();
    }

    @Override
    public void render(NotificationGraphics graphics, float partialTick) {
        graphics.sprite(BACKGROUND, 0, 0, getWidth(), getHeight());
        graphics.text(getTitle(), getTextOffset(), 7, TITLE_COLOR);
        graphics.multilineText(getWrappedMessage(), getTextOffset(), 18, -1);
        graphics.icon(getIcon(), 4, getCenterY() - 10);
    }
}
