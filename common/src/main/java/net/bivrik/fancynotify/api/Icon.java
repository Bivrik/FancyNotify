package net.bivrik.fancynotify.api;

public interface Icon {

    void draw(NotificationGraphics graphics, int x, int y);

    default void update(NotificationContext context) {}
}
