package net.bivrik.fancynotify.gui.notification;

import net.bivrik.fancynotify.FancyNotify;
import net.bivrik.fancynotify.api.Notification;
import net.bivrik.fancynotify.config.data.FiltersConfig;
import net.minecraft.network.chat.Component;

public abstract class FancyNotification extends Notification {
    protected final FiltersConfig filters;

    protected FancyNotification(Component title, Component message) {
        super(title, message);

        this.filters = FancyNotify.getInstance().getConfigManager().getFiltersConfig();
    }
}
