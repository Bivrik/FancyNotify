package net.bivrik.fancynotify.api;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public interface NotificationGraphics {

    GuiGraphics unwrap();

    void sprite(ResourceLocation sprite, int x, int y, int width, int height);

    void texture(ResourceLocation texture, int x, int y, int width, int height, int textureWidth, int textureHeight, int uOffset, int vOffset, int uWidth, int vHeight);

    default void texture(ResourceLocation texture, int x, int y, int width, int height, int textureWidth, int textureHeight, int uOffset, int vOffset) {
        texture(texture, x, y, width, height, textureWidth, textureHeight, uOffset, vOffset, width, height);
    }

    default void texture(ResourceLocation texture, int x, int y, int width, int height, int textureWidth, int textureHeight) {
        texture(texture, x, y, width, height, textureWidth, textureHeight, 0, 0, textureWidth, textureHeight);
    }

    void text(FormattedCharSequence text, int x, int y, int color);

    default void text(Component text, int x, int y, int color) {
        text(text.getVisualOrderText(), x, y, color);
    }

    default void text(String text, int x, int y, int color) {
        text(FormattedCharSequence.forward(text, Style.EMPTY), x, y, color);
    }

    void multilineText(List<FormattedCharSequence> wrappedText, int x, int y, int color);
}
