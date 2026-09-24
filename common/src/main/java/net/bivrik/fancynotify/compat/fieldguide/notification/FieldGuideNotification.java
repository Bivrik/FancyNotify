package net.bivrik.fancynotify.compat.fieldguide.notification;

import net.bivrik.fancynotify.api.NotificationGraphics;
import net.bivrik.fancynotify.compat.fieldguide.FieldGuideIconRenderer;
import net.bivrik.fancynotify.gui.notification.FancyNotification;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class FieldGuideNotification extends FancyNotification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/fieldguide/discovery");
    private static final Component MESSAGE = Component.translatable("fieldguide.toast.discovered");
    private static final int MESSAGE_COLOR = 11504732;

    private final int titleColor;
    private final FieldGuideIconRenderer iconRenderer;

    public FieldGuideNotification(Component title, int titleColor, FieldGuideIconRenderer iconRenderer) {
        super(title, MESSAGE);

        this.titleColor = titleColor;
        this.iconRenderer = iconRenderer;
    }

    @Override
    public boolean shouldDisplay() {
        return this.filters.isFieldGuideNotificationEnabled.get();
    }

    @Override
    public void draw(NotificationGraphics graphics, float partialTick) {
        graphics.sprite(BACKGROUND, 0, 0, getWidth(), getHeight());
        graphics.text(getTitle(), getTextOffset(), 7, titleColor);
        graphics.multiline(getWrappedMessage(), getTextOffset(), 16, MESSAGE_COLOR);
        iconRenderer.draw(graphics.unwrap(), 16, 17);
    }
}
