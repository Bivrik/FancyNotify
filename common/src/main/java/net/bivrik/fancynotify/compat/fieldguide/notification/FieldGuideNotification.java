package net.bivrik.fancynotify.compat.fieldguide.notification;

import net.bivrik.fancynotify.api.gui.icon.Icon;
import net.bivrik.fancynotify.api.gui.notification.NotificationGraphics;
import net.bivrik.fancynotify.gui.notification.FancyNotification;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class FieldGuideNotification extends FancyNotification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/fieldguide/discovery");
    private static final Component MESSAGE = Component.translatable("fieldguide.toast.discovered");
    private static final int MESSAGE_COLOR = 11504732;

    private final int titleColor;

    public FieldGuideNotification(Component title, int titleColor, Icon icon) {
        super(title, MESSAGE);
        setIcon(icon);

        this.titleColor = titleColor;
    }

    @Override
    public boolean shouldDisplay() {
        return this.filters.isFieldGuideNotificationEnabled.get();
    }

    @Override
    public void render(NotificationGraphics graphics, float partialTick) {
        graphics.sprite(BACKGROUND, 0, 0, getWidth(), getHeight());
        graphics.text(getTitle(), getTextOffset(), 7, titleColor);
        graphics.multilineText(getWrappedMessage(), getTextOffset(), 16, MESSAGE_COLOR);
        graphics.icon(getIcon(), 16, 17);
    }
}
