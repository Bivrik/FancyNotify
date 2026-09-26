package net.bivrik.fancynotify.gui.notification;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bivrik.fancynotify.api.gui.icon.Icon;
import net.bivrik.fancynotify.api.gui.notification.NotificationGraphics;
import net.bivrik.fancynotify.utility.Components;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class PlayerLoginNotification extends FancyNotification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/player_login");
    private static final Component MESSAGE = Components.of("gui.player_login.message");
    private static final int TITLE_COLOR = Color.yellow.getRGB();

    public PlayerLoginNotification(String playerName, ResourceLocation playerTextures, boolean hasHat) {
        super(Component.literal(playerName), MESSAGE);
        setIcon(new PlayerHeadIcon(playerTextures, hasHat));
    }

    @Override
    public boolean shouldDisplay() {
        return this.filters.isLoginPlayerNotificationEnabled.get();
    }

    @Override
    public void render(NotificationGraphics graphics, float partialTick) {
        graphics.sprite(BACKGROUND, 0, 0, getWidth(), getHeight());
        graphics.text(getTitle(), getTextOffset(), 7, TITLE_COLOR);
        graphics.multilineText(getWrappedMessage(), getTextOffset(), 18, -1);
        graphics.icon(getIcon(), 8, 8);
    }

    private record PlayerHeadIcon(ResourceLocation id, boolean hasHat) implements Icon {
        @Override
        public void draw(NotificationGraphics graphics, int x, int y) {
            graphics.texture(id, x, y, 16, 16, 64, 64, 8, 8, 8, 8);
            if (hasHat) {
                RenderSystem.enableBlend();
                graphics.texture(id, x - 1, y - 1, 18, 18, 64, 64, 40, 8, 8, 8);
                RenderSystem.disableBlend();
            }
        }
    }
}