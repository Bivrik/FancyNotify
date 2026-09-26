package net.bivrik.fancynotify.api;

import net.minecraft.resources.ResourceLocation;

public record SpriteIcon(ResourceLocation id, int width, int height) implements Icon {
    @Override
    public void draw(NotificationGraphics graphics, int x, int y) {
        graphics.sprite(id, x, y, width, height);
    }
}
