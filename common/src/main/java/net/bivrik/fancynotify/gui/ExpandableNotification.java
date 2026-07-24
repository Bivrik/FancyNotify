package net.bivrik.fancynotify.gui;

import net.bivrik.fancynotify.NotificationManager;
import net.minecraft.network.chat.Component;

public abstract class ExpandableNotification extends Notification {
    public ExpandableNotification(NotificationManager manager, Component title, Component message) {
        super(manager, title, message);
    }

    protected void expand(ExpandableNotification expansion) {}

    private void merge(ExpandableNotification notification) {
        if (this.timeTicks - this.offsetTicks < getLifeTimeTicks()) {
            this.offsetTicks = this.timeTicks;
        }

        expand(notification);
    }

    @Override
    public boolean tryMerge(Notification notification) {
        if (this.getId().equals(notification.getId()) && this.getClass().isAssignableFrom(notification.getClass())) {
            merge((ExpandableNotification) notification);
            return true;
        }
        return false;
    }
}
