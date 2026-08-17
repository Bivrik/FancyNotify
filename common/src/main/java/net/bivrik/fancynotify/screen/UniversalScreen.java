package net.bivrik.fancynotify.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class UniversalScreen extends Screen {
    protected final Screen parent;

    protected UniversalScreen(Component title, Screen parent) {
        super(title);

        this.parent = parent;
    }

    protected void setScreen(Screen screen) {
        this.minecraft.gui.setScreen(screen);
    }

    @Override
    public void onClose() {
        setScreen(parent);
    }

    @Override
    public void extractRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        drawTitle(graphics);
    }

    protected void drawTitle(GuiGraphicsExtractor graphics) {
        int xCenter = this.width / 2;
        int yOffset = 12;
        graphics.centeredText(this.font, this.title, xCenter, yOffset, -1);
    }
}
