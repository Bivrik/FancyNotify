package net.bivrik.fancynotify.gui.notification;

import com.mojang.blaze3d.platform.NativeImage;
import net.bivrik.fancynotify.api.Notification;
import net.bivrik.fancynotify.api.NotificationGraphics;
import net.bivrik.fancynotify.utility.Components;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class ScreenshotNotification extends FancyExpandableNotification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/screenshot");
    private static final ResourceLocation SCREENSHOT_PREVIEW = ResourceLocations.of("screenshot_preview");

    private static final Component TITLE = Components.of("gui.screenshot.title");
    private static final int TITLE_COLOR = new Color(43, 181, 43).getRGB();

    private final TextureManager textureManager;
    private final DynamicTexture dynamicScreenshotTexture;

    public ScreenshotNotification(NativeImage screenshotImage) {
        super(TITLE, Components.of("gui.screenshot." + ThreadLocalRandom.current().nextInt(3)));

        this.textureManager = this.minecraft.getTextureManager();
        this.dynamicScreenshotTexture = new DynamicTexture(screenshotImage);
    }

    @Override
    public boolean shouldDisplay() {
        return this.filters.isScreenshotNotificationEnabled.get();
    }

    @Override
    public int getTextOffset() {
        return super.getTextOffset() + 17;
    }

    @Override
    public void onShowing() {
        textureManager.register(SCREENSHOT_PREVIEW, dynamicScreenshotTexture);
    }

    @Override
    public void onRemoval() {
        textureManager.release(SCREENSHOT_PREVIEW);
        if (dynamicScreenshotTexture != null) {
            dynamicScreenshotTexture.close();
        }
    }

    @Override
    public void expand(Notification expansion) {
        ScreenshotNotification other = (ScreenshotNotification) expansion;

        NativeImage newScreenshotImage = other.dynamicScreenshotTexture.getPixels();
        NativeImage screenshotImage = dynamicScreenshotTexture.getPixels();
        if (newScreenshotImage != null && screenshotImage != null) {
            screenshotImage.copyFrom(newScreenshotImage);
            dynamicScreenshotTexture.upload();
        }
    }

    @Override
    public void render(NotificationGraphics graphics, float partialTick) {
        graphics.sprite(BACKGROUND, 0, 0, getWidth(), getHeight());
        graphics.text(getTitle(), getTextOffset(), 7, TITLE_COLOR);
        graphics.multilineText(getWrappedMessage(), getTextOffset(), 18, -1);
        int width = 38;
        int height = 22;
        graphics.texture(SCREENSHOT_PREVIEW,
                5, getCenterY() - height / 2,
                width, height,
                width * 4, height * 4,
                ((width * 4) - width) / 2, ((height * 4) - height) / 2);
    }
}
