package net.bivrik.fancynotify.notification;

import net.bivrik.fancynotify.api.gui.notification.NotificationManager;
import net.minecraft.client.gui.GuiGraphics;

public interface NotificationEngine extends NotificationManager {

    void update();

    void render(GuiGraphics guiGraphics);
}
