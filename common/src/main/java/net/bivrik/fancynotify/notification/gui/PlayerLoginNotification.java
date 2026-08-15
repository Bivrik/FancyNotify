package net.bivrik.fancynotify.notification.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bivrik.fancynotify.notification.Notification;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class PlayerLoginNotification extends Notification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("textures/gui/notifications/player_login.png");
    private static final Component MESSAGE = Component.translatable("fancynotify.gui.player_login.message");
    private static final int COLOR = Color.yellow.getRGB();

    private final ResourceLocation playerTexture;
    private final boolean hasHat;

    public PlayerLoginNotification(NotificationManager manager, String playerName, ResourceLocation playerTextures, boolean hasHat) {
        super(manager, Component.literal(playerName), MESSAGE);

        this.playerTexture = playerTextures;
        this.hasHat = hasHat;
    }

    @Override
    public boolean shouldDisplay() {
        return this.filtersConfig.isLoginPlayerNotificationEnabled.get();
    }

    @Override
    protected void draw(GuiGraphics guiGraphics) {
        drawBackground(guiGraphics, 128, 0);
        drawText(guiGraphics, getTitle(), getTextOffset(), 7, COLOR);
        drawMessage(guiGraphics, getTextOffset(), 18, -1);
        drawTexture(guiGraphics, playerTexture, 8, 8, 16, 16, 64, 64, 8, 8, 8, 8);
        if (hasHat) {
            RenderSystem.enableBlend();
            drawTexture(guiGraphics, playerTexture, 7, 7, 18, 18, 64, 64, 40, 8, 8, 8);
            RenderSystem.disableBlend();
        }
    }
}
