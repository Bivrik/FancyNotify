package net.bivrik.fancynotify.notification.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bivrik.fancynotify.notification.Notification;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.utility.Identifiers;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.awt.*;

public class PlayerLoginNotification extends Notification {
    private static final Identifier BACKGROUND = Identifiers.of("notifications/player_login");
    private static final Component MESSAGE = Component.translatable("fancynotify.gui.player_login.message");
    private static final int COLOR = Color.yellow.getRGB();

    private final Identifier playerTexture;
    private final boolean hasHat;

    public PlayerLoginNotification(NotificationManager manager, String playerName, Identifier playerTextures, boolean hasHat) {
        super(manager, Component.literal(playerName), MESSAGE);

        this.playerTexture = playerTextures;
        this.hasHat = hasHat;
    }

    @Override
    public boolean shouldDisplay() {
        return this.filtersConfig.isLoginPlayerNotificationEnabled.get();
    }

    @Override
    protected void draw(GuiGraphicsExtractor GuiGraphicsExtractor) {
        drawSprite(GuiGraphicsExtractor, BACKGROUND, 0, 0, getWidth(), getHeight());
        drawText(GuiGraphicsExtractor, getTitle(), getTextOffset(), 7, COLOR);
        drawMessage(GuiGraphicsExtractor, getTextOffset(), 18, -1);
        drawTexture(GuiGraphicsExtractor, playerTexture, 8, 8, 16, 16, 64, 64, 8, 8, 8, 8);
        if (hasHat) {
            drawTexture(GuiGraphicsExtractor, playerTexture, 7, 7, 18, 18, 64, 64, 40, 8, 8, 8);
        }
    }
}
