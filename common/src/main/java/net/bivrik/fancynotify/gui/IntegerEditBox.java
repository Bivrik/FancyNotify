package net.bivrik.fancynotify.gui;

import com.google.common.primitives.Ints;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class IntegerEditBox extends EditBox {
    private final Font font;
    private int messageWidth;

    public IntegerEditBox(Font font, int x, int y, int width, int height, EditBox editBox, Component message, int initialValue) {
        super(font, x, y, width, height, editBox, message);

        this.font = font;
        this.messageWidth = font.width(message);
        setIntegerValue(initialValue);
    }

    public void setIntegerResponder(Consumer<Integer> consumer) {
        Integer value = Ints.tryParse(this.getValue());
        if (value != null) {
            consumer.accept(value);
        }
    }

    public void setIntegerValue(int value) {
        setValue(String.valueOf(value));
    }

    @Override
    public void setMessage(@NotNull Component message) {
        super.setMessage(message);
        messageWidth = font.width(message);
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.fill(this.getX() + 3, this.getY(), this.getX() + 3 + messageWidth + 2, this.getY() + 1, 0xFF000000);
        guiGraphics.drawString(font, this.getMessage(), this.getX() + 4, this.getY() - 4, this.isFocused() ? -1 : 0xFF999999);
    }
}
