package net.bivrik.fancynotify.api.gui.icon;

import net.bivrik.fancynotify.api.gui.notification.NotificationGraphics;
import net.minecraft.world.item.ItemStack;

public record ItemIcon(ItemStack item) implements Icon {
    @Override
    public void draw(NotificationGraphics graphics, int x, int y) {
        graphics.unwrap().renderFakeItem(item, x, y);
    }
}
