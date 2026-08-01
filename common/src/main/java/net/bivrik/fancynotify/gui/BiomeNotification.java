package net.bivrik.fancynotify.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bivrik.fancynotify.NotificationManager;
import net.bivrik.fancynotify.ResourceLocations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

public class BiomeNotification extends ExpandableNotification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/biome");
    private static final int COLOR = new Color(41, 92, 38).getRGB();

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
    protected void expand(ExpandableNotification expansion) {
        if (expansion instanceof BiomeNotification biomeNotification) {
            setDisplay(biomeNotification.getTitle(), Component.empty());
            icon = biomeNotification.icon;
        }
    }

    @Override
    public void draw(GuiGraphics guiGraphics) {
        drawSprite(guiGraphics, BACKGROUND, 0, 0, getWidth(), getHeight());
        drawText(guiGraphics, getTitle(), getTextOffset(), 8, COLOR);
        PoseStack stack = guiGraphics.pose();
        stack.pushPose();
        stack.scale(0.85f, 0.85f, 1);
        guiGraphics.renderFakeItem(icon, 10, getCenterY() - 6);
        stack.popPose();
    }
}
