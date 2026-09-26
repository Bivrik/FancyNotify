package net.bivrik.fancynotify.api.gui.icon;

import net.bivrik.fancynotify.api.gui.notification.NotificationGraphics;
import net.minecraft.resources.ResourceLocation;

public record SpriteIcon(ResourceLocation id, int width, int height) implements Icon {
    @Override
    public void draw(NotificationGraphics graphics, int x, int y) {
        graphics.sprite(id, x, y, width, height);
    }
}
