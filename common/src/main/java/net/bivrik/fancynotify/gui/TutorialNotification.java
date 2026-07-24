package net.bivrik.fancynotify.gui;

import net.bivrik.fancynotify.NotificationManager;
import net.bivrik.fancynotify.ResourceLocations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class TutorialNotification extends Notification {
    /*private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/advancement");
    private final TutorialToast.Icons icon;
    private final Component titleText;
    private final Component descriptionText;*/

    public TutorialNotification(NotificationManager manager, TutorialToast.Icons icon, Component titleText, Component descriptionText) {
        super(manager, titleText, descriptionText);

        /*this.icon = icon;
        this.titleText = titleText;
        this.descriptionText = descriptionText;*/
    }

    @Override
    public void draw(GuiGraphics guiGraphics) {
        /*drawSprite(guiGraphics, BACKGROUND, 0, 0, this.getWidth(), this.getHeight());
        drawString(guiGraphics, titleText, 29, 7, Color.yellow.getRGB(), false);
        drawString(guiGraphics, descriptionText, 29, 18, -1, false);
        icon.render(guiGraphics, 4, this.getCenterY() - 5);*/
    }
}
