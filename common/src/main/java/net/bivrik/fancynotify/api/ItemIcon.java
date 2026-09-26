package net.bivrik.fancynotify.api;

import net.minecraft.world.item.ItemStack;

public record ItemIcon(ItemStack item) implements Icon {
    @Override
    public void draw(NotificationGraphics graphics, int x, int y) {
        graphics.unwrap().renderFakeItem(item, x, y);
    }
}
