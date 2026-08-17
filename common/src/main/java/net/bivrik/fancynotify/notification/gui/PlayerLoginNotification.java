package net.bivrik.fancynotify.notification.gui;

import net.bivrik.fancynotify.notification.Notification;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.utility.Identifiers;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.component.ResolvableProfile;

import java.awt.*;

public class PlayerLoginNotification extends Notification {
    private static final Identifier BACKGROUND = Identifiers.of("notifications/player_login");
    private static final Component MESSAGE = Component.translatable("fancynotify.gui.player_login.message");
    private static final int COLOR = Color.yellow.getRGB();

    private final ResolvableProfile profile;
    private final boolean hasHat;

    public PlayerLoginNotification(NotificationManager manager, String playerName, ResolvableProfile profile, boolean hasHat) {
        super(manager, Component.literal(playerName), MESSAGE);

        this.profile = profile;
        this.hasHat = hasHat;
    }

    @Override
    public boolean shouldDisplay() {
        return this.filtersConfig.isLoginPlayerNotificationEnabled.get();
    }

    @Override
    protected void draw(GuiGraphicsExtractor graphics) {
        drawSprite(graphics, BACKGROUND, 0, 0, getWidth(), getHeight());
        drawText(graphics, getTitle(), getTextOffset(), 7, COLOR);
        drawMessage(graphics, getTextOffset(), 18, -1);

        PlayerSkinRenderCache cache = this.minecraft.playerSkinRenderCache();
        PlayerSkinRenderCache.RenderInfo renderInfo = cache.getOrDefault(profile);
        Identifier playerTexture = renderInfo.playerSkin().body().texturePath();
        drawTexture(graphics, playerTexture, 8, 8, 16, 16, 64, 64, 8, 8, 8, 8);
        if (hasHat) {
            drawTexture(graphics, playerTexture, 7, 7, 18, 18, 64, 64, 40, 8, 8, 8);
        }
    }
}
