package net.bivrik.fancynotify.notification.gui;

import net.bivrik.fancynotify.notification.Notification;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.utility.Identifiers;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.component.ResolvableProfile;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class FriendNotification extends Notification {
    private static final Identifier BACKGROUND = Identifiers.of("notifications/friend");

    private final ResolvableProfile profile;

    public FriendNotification(NotificationManager manager, @NotNull Component title, ResolvableProfile profile) {
        this.profile = profile;
        super(manager, title, Component.empty());
    }

    @Override
    protected int getTextOffset() {
        if (profile == null) {
            return 9;
        } else {
            return super.getTextOffset();
        }
    }

    @Override
    protected void draw(GuiGraphicsExtractor graphics) {
        drawSprite(graphics, BACKGROUND, 0, 0, getWidth(), getHeight());
        drawText(graphics, getTitle(), getTextOffset(), 8, new Color(103, 142, 101).getRGB());

        if (profile != null) {
            PlayerSkinRenderCache cache = this.minecraft.playerSkinRenderCache();
            PlayerSkinRenderCache.RenderInfo renderInfo = cache.getOrDefault(profile);
            Identifier playerTexture = renderInfo.playerSkin().body().texturePath();
            int x = 7;
            int y = 3;
            int width = 17;
            int height = 17;
            drawTexture(graphics, playerTexture, x, y, width, height, 64, 64, 8, 8, 8, 8);
            drawTexture(graphics, playerTexture, x - 1, y - 1, width + 2, height + 2, 64, 64, 40, 8, 8, 8);
        }
    }
}
