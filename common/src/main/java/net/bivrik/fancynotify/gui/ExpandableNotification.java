package net.bivrik.fancynotify.gui;

import net.bivrik.fancynotify.NotificationManager;
import net.minecraft.network.chat.Component;

public abstract class ExpandableNotification extends Notification {
    public ExpandableNotification(NotificationManager manager, Component title, Component message) {
        super(manager, title, message);
    }

    protected void expand(ExpandableNotification expansion) {}

    private void merge(ExpandableNotification notification) {
        if (this.timeTicks - this.offsetTicks < getLifeTimeTicks() * this.minecraft.options.notificationDisplayTime().get() - this.generalConfig.animationDuration.get()) {
            this.offsetTicks = this.timeTicks;
        }

        expand(notification);
    }

    @Override
    public boolean tryMerge(Notification notification) {
        if (getId().equals(notification.getId()) && getClass().isAssignableFrom(notification.getClass())) {
            merge((ExpandableNotification) notification);
            return true;
        }
        return false;
    }
}
