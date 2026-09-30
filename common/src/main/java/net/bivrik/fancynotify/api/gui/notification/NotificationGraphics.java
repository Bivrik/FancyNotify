package net.bivrik.fancynotify.api.gui.notification;

import net.bivrik.fancynotify.api.gui.icon.Icon;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * Wrapper around {@link GuiGraphics} with helper methods to simplify draw calls, as well as handling animation transformations
 * <br>
 * Created fresh every frame, do not cache it
 */
public interface NotificationGraphics {

    /**
     * Returns the raw {@link GuiGraphics} from wrapper when helper methods aren't enough. For example {@link GuiGraphics#renderFakeItem(ItemStack, int, int)}
     *
     * @return the unwrapped {@link GuiGraphics}
     */
    GuiGraphics unwrap();

    /**
     * Draws the sprite. Used for UI elements
     *
     * @param sprite the sprite to draw
     * @param x left edge
     * @param y top edge
     * @param width how wide output sprite is on the screen
     * @param height how tall output sprite is on the screen
     */
    void sprite(ResourceLocation sprite, int x, int y, int width, int height);

    /**
     * Draws the texture with slicing and UV offsets
     *
     * @param texture the texture to draw
     * @param x left edge
     * @param y top edge
     * @param width how wide output texture is on the screen
     * @param height how tall output texture is on the screen
     * @param textureWidth the full width of the texture file
     * @param textureHeight the full height of the texture file
     * @param uOffset horizontal pixel offset into the texture
     * @param vOffset vertical pixel offset into the texture
     * @param uWidth how many texture pixels wide the slice is
     * @param vHeight how many texture pixels tall the slice is
     */
    void texture(ResourceLocation texture, int x, int y, int width, int height, int textureWidth, int textureHeight, int uOffset, int vOffset, int uWidth, int vHeight);

    /**
     * Draws the texture with slice size matching width and height
     * <br>
     * See {@link #texture(ResourceLocation, int, int, int, int, int, int, int, int, int, int)}
     */
    default void texture(ResourceLocation texture, int x, int y, int width, int height, int textureWidth, int textureHeight, int uOffset, int vOffset) {
        texture(texture, x, y, width, height, textureWidth, textureHeight, uOffset, vOffset, width, height);
    }

    /**
     * Draws the texture without slicing
     * <br>
     * See {@link #texture(ResourceLocation, int, int, int, int, int, int, int, int, int, int)}
     */
    default void texture(ResourceLocation texture, int x, int y, int width, int height, int textureWidth, int textureHeight) {
        texture(texture, x, y, width, height, textureWidth, textureHeight, 0, 0, textureWidth, textureHeight);
    }

    /**
     * Draws an icon
     *
     * @param icon the icon to draw
     * @param x left edge
     * @param y top edge
     */
    void icon(Icon icon, int x, int y);

    /**
     * Draws a formatted char sequence as a single line
     *
     * @param text the text to draw
     * @param x left edge
     * @param y top edge
     * @param color the text color in packed RGBA
     */
    void text(FormattedCharSequence text, int x, int y, int color);

    /**
     * Draws a component as a single line
     * <br>
     * See {@link #text(FormattedCharSequence, int, int, int)}
     */
    default void text(Component text, int x, int y, int color) {
        text(text.getVisualOrderText(), x, y, color);
    }

    /**
     * Draws a string as a single line
     * <br>
     * See {@link #text(FormattedCharSequence, int, int, int)}
     */
    default void text(String text, int x, int y, int color) {
        text(FormattedCharSequence.forward(text, Style.EMPTY), x, y, color);
    }

    /**
     * Draws several lines from top to bottom. You can wrap lines using {@link Font#split(FormattedText, int)}, or just use {@link Notification#getWrappedMessage()}
     *
     * @param wrappedText the lines to draw
     * @param x left edge
     * @param y top edge
     * @param color the text color in packed RGBA
     */
    void multilineText(List<FormattedCharSequence> wrappedText, int x, int y, int color);
}
