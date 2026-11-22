package net.bivrik.fancynotify.api.gui.icon;

import net.bivrik.fancynotify.api.gui.notification.NotificationGraphics;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public record ItemIcon(ItemStack item) implements Icon {
    @Override
    public void draw(NotificationGraphics graphics, int x, int y) {
        graphics.unwrap().renderFakeItem(item, x, y);
    }
}
