package net.bivrik.fancynotify.gui;

import com.mojang.blaze3d.platform.NativeImage;
import net.bivrik.fancynotify.NotificationManager;
import net.bivrik.fancynotify.ResourceLocations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.Random;

public class ScreenshotNotification extends ExpandableNotification {
    private static final Random random = new Random();
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/screenshot");
    private static final ResourceLocation SCREENSHOT_PREVIEW = ResourceLocations.of("screenshot_preview");
    private final TextureManager textureManager;

    public ScreenshotNotification(NotificationManager manager, NativeImage screenshotImage) {
        super(manager);

        var title = Component.translatable("fancynotify.gui.screenshot.title");
        var message = Component.translatable("fancynotify.gui.screenshot." + random.nextInt(3));
        this.setDisplay(title, message);
        this.textureManager = this.notificationManager.getMinecraft().getTextureManager();
        DynamicTexture screenshotPreview = new DynamicTexture(screenshotImage);
        this.textureManager.register(SCREENSHOT_PREVIEW, screenshotPreview);
    }

    @Override
    public boolean shouldDisplay() {
        return this.filtersConfig.isScreenshotNotificationEnabled.get();
    }

    @Override
    protected void onRemoval() {
        textureManager.release(SCREENSHOT_PREVIEW);
    }

    @Override
    public void draw(GuiGraphics guiGraphics) {
        drawSprite(guiGraphics, BACKGROUND, 0, 0, this.getWidth(), this.getHeight());
        drawText(guiGraphics, this.title, 29 + 17, 7, new Color(43, 181, 43).getRGB());
        drawText(guiGraphics, this.message, 29 + 17, 18, -1);
        int width = 38;
        int height = 22;
        drawTexture(guiGraphics, SCREENSHOT_PREVIEW, 5, 5, width, height, width * 4, height * 4, ((width * 4) - width) / 2, ((height * 4) - height) / 2);
    }
}
