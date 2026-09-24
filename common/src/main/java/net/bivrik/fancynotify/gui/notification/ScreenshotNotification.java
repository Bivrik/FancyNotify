package net.bivrik.fancynotify.gui.notification;

import com.mojang.blaze3d.platform.NativeImage;
import net.bivrik.fancynotify.FancyNotify;
import net.bivrik.fancynotify.api.Notification;
import net.bivrik.fancynotify.api.NotificationGraphics;
import net.bivrik.fancynotify.core.Constants;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.Random;

public class ScreenshotNotification extends FancyNotification {
    private static final Random RANDOM = new Random();
    private static final Component TITLE = Component.translatable("fancynotify.gui.screenshot.title");
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/screenshot");
    private static final ResourceLocation SCREENSHOT_PREVIEW = ResourceLocations.of("screenshot_preview");
    public static final int TITLE_COLOR = new Color(43, 181, 43).getRGB();

    private final TextureManager textureManager;

    public ScreenshotNotification(NativeImage screenshotImage) {
        super(TITLE, Component.translatable(Constants.MOD_ID + ".gui.screenshot." + RANDOM.nextInt(3)));

        this.textureManager = this.minecraft.getTextureManager();
        DynamicTexture screenshotPreview = new DynamicTexture(screenshotImage);
        this.textureManager.register(SCREENSHOT_PREVIEW, screenshotPreview);
    }

    @Override
    public boolean shouldDisplay() {
        return this.filters.isScreenshotNotificationEnabled.get();
    }

    @Override
    public void onRemoval() {
        super.onRemoval();

        textureManager.release(SCREENSHOT_PREVIEW);
    }

    @Override
    public int getTextOffset() {
        return super.getTextOffset() + 17;
    }

    @Override
    public void draw(NotificationGraphics graphics, float partialTick) {
        graphics.sprite(BACKGROUND, 0, 0, getWidth(), getHeight());
        graphics.text(getTitle(), getTextOffset(), 7, TITLE_COLOR);
        graphics.multiline(getWrappedMessage(), getTextOffset(), 18, -1);
        int width = 38;
        int height = 22;
        graphics.texture(SCREENSHOT_PREVIEW, 5, getCenterY() - height / 2, width, height, width * 4, height * 4, ((width * 4) - width) / 2, ((height * 4) - height) / 2);
    }
}
