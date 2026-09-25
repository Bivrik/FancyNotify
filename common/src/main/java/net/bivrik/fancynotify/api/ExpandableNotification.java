package net.bivrik.fancynotify.api;

import net.minecraft.network.chat.Component;

public abstract class ExpandableNotification extends Notification {
    protected ExpandableNotification(Component title, Component message) {
        super(title, message);
    }

    @Override
    public final boolean canExpandFrom(Notification other) {
        return getClass() == other.getClass() && getId().equals(other.getId());
    }

    public abstract void expand(Notification expansion);
}
