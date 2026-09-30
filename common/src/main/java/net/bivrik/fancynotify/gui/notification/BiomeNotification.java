package net.bivrik.fancynotify.gui.notification;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bivrik.fancynotify.api.gui.icon.ItemIcon;
import net.bivrik.fancynotify.api.gui.notification.Notification;
import net.bivrik.fancynotify.api.gui.notification.NotificationGraphics;
import net.bivrik.fancynotify.utility.Color;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class BiomeNotification extends FancyExpandableNotification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/biome");
    private static final Component MESSAGE = Component.empty();
    private static final int TITLE_COLOR = Color.create(41, 92, 38);

    public BiomeNotification(Component biomeName, ItemStack icon) {
        super(biomeName, Component.empty());
        setIcon(new ItemIcon(icon));
    }

    @Override
    public boolean shouldDisplay() {
        return this.filters.isBiomeNotificationEnabled.get();
    }

    @Override
    public void expand(Notification expansion) {
        BiomeNotification other = (BiomeNotification) expansion;
        
        setDisplay(other.getTitle(), MESSAGE);
        setIcon(other.getIcon());
    }

    @Override
    public void render(NotificationGraphics graphics, float partialTick) {
        graphics.sprite(BACKGROUND, 0, 0, getWidth(), getHeight());
        graphics.text(getTitle(), getTextOffset(), 8, TITLE_COLOR);
        GuiGraphics guiGraphics = graphics.unwrap();
        PoseStack stack = guiGraphics.pose();
        stack.pushPose();
        stack.scale(0.85f, 0.85f, 1);
        graphics.icon(getIcon(), 10, getCenterY() - 6);
        stack.popPose();
    }
}
