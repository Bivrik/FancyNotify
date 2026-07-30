package net.bivrik.fancynotify.event;

import net.bivrik.fancynotify.eventbus.Event;

public class NotificationWidthChangedEvent extends Event {
    private final int width;

    public NotificationWidthChangedEvent(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }
}
