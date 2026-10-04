package net.bivrik.fancynotify.notification;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import net.bivrik.fancynotify.api.gui.icon.Icon;
import net.bivrik.fancynotify.api.gui.notification.NotificationGraphics;
import net.bivrik.fancynotify.notification.animation.NotificationAnimator;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public record NotificationGraphicsImpl(GuiGraphics unwrap, Font font, NotificationAnimator animator, float notificationsTransparency) implements NotificationGraphics {
    private float getMultipliedAlpha() {
        return animator.getAlpha() * notificationsTransparency;
    }

    @Override
    public void sprite(ResourceLocation sprite, int x, int y, int width, int height) {
        if (getMultipliedAlpha() >= 0.99f) {
            unwrap.blitSprite(sprite, x, y, width, height);
            return;
        }

        RenderSystem.enableBlend();
        unwrap.setColor(1.0f, 1.0f, 1.0f, getMultipliedAlpha());
        unwrap.blitSprite(sprite, x, y, width, height);
        unwrap.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.disableBlend();
    }

    @Override
    public void texture(ResourceLocation texture, int x, int y, int width, int height, int textureWidth, int textureHeight, int uOffset, int vOffset, int uWidth, int vHeight) {
        if (getMultipliedAlpha() >= 0.99f) {
            unwrap.blit(texture, x, y, width, height, uOffset, vOffset, uWidth, vHeight, textureWidth, textureHeight);
            return;
        }

        RenderSystem.enableBlend();
        unwrap.setColor(1.0f, 1.0f, 1.0f, getMultipliedAlpha());
        unwrap.blit(texture, x, y, width, height, uOffset, vOffset, uWidth, vHeight, textureWidth, textureHeight);
        unwrap.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.disableBlend();
    }

    @Override
    public void icon(Icon icon, int x, int y) {
        if (icon == null) {
            return;
        }

        icon.draw(this, x, y);
    }

    @Override
    public void text(FormattedCharSequence text, int x, int y, int color) {
        if (getMultipliedAlpha() >= 0.99f) {
            unwrap.drawString(font, text, x, y, color, false);
            return;
        }

        // Am I not understanding something
        // or why is it so complicated?
        // I mean... why doesn't it work
        // as intended from the start???
        // Why without all of this there is a bug,
        // when using guiGraphics.setColor(),
        // it makes all the tooltips with the same color?
        int iAlpha = Math.clamp((int) (getMultipliedAlpha() * 255), 25, 255);
        int alphaColor = (iAlpha << 24) | (color & 0x00FFFFFF);
        MultiBufferSource.BufferSource isolatedBuffer = MultiBufferSource.immediate(new ByteBufferBuilder(256));
        RenderSystem.enableBlend();
        font.drawInBatch(
                text, x, y, alphaColor, false,
                unwrap.pose().last().pose(),
                isolatedBuffer,
                Font.DisplayMode.NORMAL,
                0, 15728880
        );
        isolatedBuffer.endBatch();
        RenderSystem.disableBlend();
    }

    @Override
    public void multilineText(List<FormattedCharSequence> wrappedText, int x, int y, int color) {
        for (int i = 0; i < wrappedText.size(); i++) {
            FormattedCharSequence line = wrappedText.get(i);
            text(line, x, y + i * 9, color);
        }
    }
}
