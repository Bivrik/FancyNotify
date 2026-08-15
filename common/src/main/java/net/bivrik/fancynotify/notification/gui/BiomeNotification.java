package net.bivrik.fancynotify.notification.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bivrik.fancynotify.notification.ExpandableNotification;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.utility.Identifiers;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3x2fStack;

import java.awt.*;

public class BiomeNotification extends ExpandableNotification {
    private static final Identifier BACKGROUND = Identifiers.of("notifications/biome");
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
    public void draw(GuiGraphicsExtractor GuiGraphicsExtractor) {
        drawSprite(GuiGraphicsExtractor, BACKGROUND, 0, 0, getWidth(), getHeight());
        drawText(GuiGraphicsExtractor, getTitle(), getTextOffset(), 8, COLOR);
        Matrix3x2fStack stack = GuiGraphicsExtractor.pose();
        stack.pushMatrix();
        stack.scale(0.85f, 0.85f);
        GuiGraphicsExtractor.fakeItem(icon, 10, getCenterY() - 6);
        stack.popMatrix();
    }
}
