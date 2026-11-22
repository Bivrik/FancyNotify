package net.bivrik.fancynotify.api.gui.notification;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public abstract class ExpandableNotification extends Notification {
    protected ExpandableNotification(Component title, Component message) {
        super(title, message);
    }

    public final boolean canExpandFrom(Notification other) {
        return getClass() == other.getClass() && getId().equals(other.getId());
    }

    public abstract void expand(Notification expansion);
}
