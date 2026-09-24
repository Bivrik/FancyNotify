package net.bivrik.fancynotify.gui.notification;

import net.bivrik.fancynotify.FancyNotify;
import net.bivrik.fancynotify.api.ExpandableNotification;
import net.bivrik.fancynotify.config.data.FiltersConfig;
import net.minecraft.network.chat.Component;

public abstract class FancyExpandableNotification extends ExpandableNotification {
    protected final FiltersConfig filters;

    protected FancyExpandableNotification(Component title, Component message) {
        super(title, message);

        this.filters = FancyNotify.getInstance().getConfigManager().getFiltersConfig();
    }
}
