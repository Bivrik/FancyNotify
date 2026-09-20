package net.bivrik.compat.fieldguide.notification;

import net.bivrik.compat.fieldguide.FieldGuideIconRenderer;
import net.bivrik.fancynotify.notification.Notification;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class FieldGuideNotification extends Notification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("textures/gui/compatibilities.png");
    private static final Component MESSAGE = Component.translatable("fieldguide.toast.discovered");
    private static final int MESSAGE_COLOR = 11504732;

    private final int titleColor;
    private final FieldGuideIconRenderer iconRenderer;

    public FieldGuideNotification(NotificationManager manager, Component title, int titleColor, FieldGuideIconRenderer iconRenderer) {
        super(manager, title, MESSAGE);

        this.titleColor = titleColor;
        this.iconRenderer = iconRenderer;
    }

    @Override
    public boolean shouldDisplay() {
        return this.filtersConfig.isFieldGuideNotificationEnabled.get();
    }

    @Override
    protected void draw(GuiGraphics guiGraphics) {
        drawBackground(guiGraphics, BACKGROUND, 0, 64);
        drawText(guiGraphics, getTitle(), getTextOffset(), 7, titleColor);
        drawMessage(guiGraphics, getTextOffset(), 16, MESSAGE_COLOR);
        iconRenderer.draw(guiGraphics, 16, 17);
    }
}
