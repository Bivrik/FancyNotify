package net.bivrik.fancynotify.gui;

import net.bivrik.fancynotify.NotificationManager;
import net.bivrik.fancynotify.ResourceLocations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.awt.*;

public class PlayerLoginNotification extends Notification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/player_login");
    private static final ItemStack ICON = Items.YELLOW_WOOL.getDefaultInstance();
    private static final int COLOR = Color.yellow.getRGB();

    public PlayerLoginNotification(NotificationManager manager, Component playerDisplayName) {
        super(manager, playerDisplayName, Component.literal("Joined!"));
    }

    @Override
    protected void draw(GuiGraphics guiGraphics) {
        drawSprite(guiGraphics, BACKGROUND, 0, 0, getWidth(), getHeight());
        drawText(guiGraphics, getTitle(), getTextOffset(), 7, COLOR);
        drawMessage(guiGraphics, getTextOffset(), 18, -1);
        guiGraphics.renderFakeItem(ICON, 8, getCenterY() - 8);
    }
}
