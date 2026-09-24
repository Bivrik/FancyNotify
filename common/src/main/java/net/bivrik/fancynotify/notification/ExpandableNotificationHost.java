package net.bivrik.fancynotify.notification;

import net.bivrik.fancynotify.api.ExpandableNotification;
import net.bivrik.fancynotify.api.Notification;
import net.bivrik.fancynotify.config.ConfigManager;
import net.minecraft.client.Minecraft;

public class ExpandableNotificationHost extends NotificationHost {
    private final ExpandableNotification content;

    public ExpandableNotificationHost(ExpandableNotification content, Minecraft minecraft, ConfigManager configManager) {
        super(content, minecraft, configManager);

        this.content = content;
    }

    @Override
    public boolean tryMerge(Notification other) {
        if (!content.canExpandFrom(other)) {
            return false;
        }

        double lifeTimeTicks = getLifeTimeTicks() * this.minecraft.options.notificationDisplayTime().get() - this.config.animationDuration.get();
        if (this.timeTicks - this.offsetTicks < lifeTimeTicks) {
            this.offsetTicks = this.timeTicks;
        }

        content.expand(other);

        return true;
    }
}
