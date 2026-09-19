package net.bivrik.fancynotify.api;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public interface INotificationManager extends IClientNotificationManager {

    void update();

    void render(GuiGraphicsExtractor graphics);
}
