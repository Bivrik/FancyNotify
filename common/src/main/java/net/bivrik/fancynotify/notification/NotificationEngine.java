package net.bivrik.fancynotify.notification;

import net.bivrik.fancynotify.api.NotificationManager;
import net.minecraft.client.gui.GuiGraphics;

public interface NotificationEngine extends NotificationManager {

    void update();

    void render(GuiGraphics guiGraphics);
}
