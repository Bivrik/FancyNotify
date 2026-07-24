package net.bivrik.fancynotify.gui;

import net.bivrik.fancynotify.NotificationManager;
import net.bivrik.fancynotify.ResourceLocations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

public class BiomeNotification extends ExpandableNotification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/biome");
    private static final int COLOR = new Color(14, 94, 14).getRGB();

    private ItemStack icon;

    public BiomeNotification(NotificationManager manager, Component biomeName, ItemStack icon) {
        super(manager, biomeName, Component.empty());

        this.icon = icon;
    }

    @Override
    public boolean shouldDisplay() {
        return this.filtersConfig.isBiomeNotificationEnabled.get();
    }

    @Override
    public int getHeight() {
        return 23;
    }

    @Override
    protected void expand(ExpandableNotification expansion) {
        if (expansion instanceof BiomeNotification biomeNotification) {
            this.setDisplay(biomeNotification.title, Component.empty());
            this.icon = biomeNotification.icon;
        }
    }

    @Override
    public void draw(GuiGraphics guiGraphics) {
        drawSprite(guiGraphics, BACKGROUND, 0, 0, this.getWidth(), this.getHeight());
        drawText(guiGraphics, title, this.getTextOffset(), 8, COLOR);
        guiGraphics.renderFakeItem(icon, 8, this.getCenterY() - 8);
    }
}
