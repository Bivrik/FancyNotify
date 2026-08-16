package net.bivrik.fancynotify.notification.gui;

import com.mojang.blaze3d.platform.NativeImage;
import net.bivrik.fancynotify.core.Constants;
import net.bivrik.fancynotify.notification.ExpandableNotification;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.utility.Identifiers;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.awt.*;
import java.util.Random;

public class ScreenshotNotification extends ExpandableNotification {
    private static final Random RANDOM = new Random();
    private static final Component TITLE = Component.translatable("fancynotify.gui.screenshot.title");
    private static final Identifier BACKGROUND = Identifiers.of("notifications/screenshot");
    private static final Identifier SCREENSHOT_PREVIEW = Identifiers.of("screenshot_preview");

    private final TextureManager textureManager;

    public ScreenshotNotification(NotificationManager manager, NativeImage screenshotImage) {
        super(manager, TITLE, Component.translatable(Constants.MOD_ID + ".gui.screenshot." + RANDOM.nextInt(3)));

        textureManager = this.minecraft.getTextureManager();
        DynamicTexture screenshotPreview = new DynamicTexture(SCREENSHOT_PREVIEW::toString, screenshotImage);
        textureManager.register(SCREENSHOT_PREVIEW, screenshotPreview);
    }

    @Override
    public boolean shouldDisplay() {
        return this.filtersConfig.isScreenshotNotificationEnabled.get();
    }

    @Override
    public void onRemoval() {
        super.onRemoval();

        textureManager.release(SCREENSHOT_PREVIEW);
    }

    @Override
    protected int getTextOffset() {
        return super.getTextOffset() + 17;
    }

    @Override
    public void draw(GuiGraphicsExtractor GuiGraphicsExtractor) {
        drawSprite(GuiGraphicsExtractor, BACKGROUND, 0, 0, getWidth(), getHeight());
        drawText(GuiGraphicsExtractor, getTitle(), getTextOffset(), 7, new Color(43, 181, 43).getRGB());
        drawMessage(GuiGraphicsExtractor, getTextOffset(), 18, -1);
        int width = 38;
        int height = 22;
        drawTexture(GuiGraphicsExtractor, SCREENSHOT_PREVIEW, 5, getCenterY() - height / 2, width, height, width * 4, height * 4, ((width * 4) - width) / 2, ((height * 4) - height) / 2);
    }
}
