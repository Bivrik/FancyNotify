package net.bivrik.fancynotify.api;

import net.minecraft.client.gui.GuiGraphics;

public interface INotificationManager extends IClientNotificationManager {
    void update();
    void render(GuiGraphics graphics, float partialTick);
}
