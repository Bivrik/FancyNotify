package net.bivrik.fancynotify.api.gui.icon;

import net.bivrik.fancynotify.api.gui.notification.NotificationGraphics;

/**
 * Icon representable, whether it's an item {@link ItemIcon}, sprite {@link SpriteIcon}, or anything else you want to draw as an icon
 * <p>
 * No heavy calculations because {@link #draw(NotificationGraphics, int, int)} is called every frame when notification is rendered
 */
public interface Icon {

    /**
     * Draws icon at the given spot. Coordinates {@code (0,0)} means icon will be drawn in the top-left corner of notification
     * @param graphics the canvas to draw onto
     * @param x left edge, relative to the notification
     * @param y top edge, relative to the notification
     */
    void draw(NotificationGraphics graphics, int x, int y);
}
