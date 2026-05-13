package net.bivrik.fancynotify.gui;

import net.bivrik.fancynotify.NotificationManager;
import net.bivrik.fancynotify.ResourceLocations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class MusicNotification extends Notification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/music");
    private static final ResourceLocation ICON = ResourceLocations.of("icons/music");

    public MusicNotification(NotificationManager manager, Component musicText) {
        super(manager);

        String[] musicInfo = musicText.getString().split(" - ");
        Component artist = Component.literal(musicInfo[0]);
        Component title = Component.literal(musicInfo[1]);
        this.setDisplay(artist, title);
    }

    @Override
    public void draw(GuiGraphics guiGraphics) {
        drawSprite(guiGraphics, BACKGROUND, 0, 0, this.getWidth(), this.getHeight());
        drawText(guiGraphics, this.title, 29, 7, Color.cyan.getRGB());
        drawText(guiGraphics, this.message, 29, 18, -1);
        drawSprite(guiGraphics, ICON, 4, this.getCenterY() - 10, 21, 21);
    }
}
