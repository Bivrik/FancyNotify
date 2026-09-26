package net.bivrik.fancynotify.gui.notification;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bivrik.fancynotify.api.NotificationGraphics;
import net.bivrik.fancynotify.utility.Components;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class PlayerLoginNotification extends FancyNotification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/player_login");
    private static final Component MESSAGE = Components.of("gui.player_login.message");
    private static final int TITLE_COLOR = Color.yellow.getRGB();

    private final ResourceLocation playerTexture;
    private final boolean hasHat;

    public PlayerLoginNotification(String playerName, ResourceLocation playerTextures, boolean hasHat) {
        super(Component.literal(playerName), MESSAGE);

        this.playerTexture = playerTextures;
        this.hasHat = hasHat;
    }

    @Override
    public boolean shouldDisplay() {
        return this.filters.isLoginPlayerNotificationEnabled.get();
    }

    @Override
    public void render(NotificationGraphics graphics, float partialTick) {
        graphics.sprite(BACKGROUND, 0, 0, getWidth(), getHeight());
        graphics.text(getTitle(), getTextOffset(), 7, TITLE_COLOR);
        graphics.multiline(getWrappedMessage(), getTextOffset(), 18, -1);
        graphics.texture(playerTexture, 8, 8, 16, 16, 64, 64, 8, 8, 8, 8);
        if (hasHat) {
            RenderSystem.enableBlend();
            graphics.texture(playerTexture, 7, 7, 18, 18, 64, 64, 40, 8, 8, 8);
            RenderSystem.disableBlend();
        }
    }
}
