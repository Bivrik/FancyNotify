package net.bivrik.fancynotify.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UniversalScreen extends Screen {
    private final List<Renderable> renderables = new ArrayList<>();
    protected final Screen parent;

    protected UniversalScreen(Component title, Screen parent) {
        super(title);

        this.parent = parent;
    }

    protected void setScreen(Screen screen) {
        Objects.requireNonNull(this.minecraft).setScreenAndShow(screen);
    }

    protected <T extends GuiEventListener & Renderable & NarratableEntry> T addSimpleWidget(T widget) {
        addSimpleRenderable(widget);
        return super.addWidget(widget);
    }

    protected <T extends GuiEventListener & Renderable & NarratableEntry> void removeSimpleWidget(T widget) {
        removeSimpleRenderable(widget);
        super.removeWidget(widget);
    }

    protected <T extends Renderable> T addSimpleRenderable(T renderable) {
        renderables.add(renderable);
        return renderable;
    }

    protected <T extends Renderable> void removeSimpleRenderable(T renderable) {
        renderables.remove(renderable);
    }

    @Override
    protected void clearWidgets() {
        renderables.clear();
        super.clearWidgets();
    }

    @Override
    public void onClose() {
        setScreen(parent);
    }

    @Override
    public void extractRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        drawRenderables(graphics, mouseX, mouseY, partialTick);
        drawTitle(graphics);
    }

    protected void drawRenderables(GuiGraphicsExtractor GuiGraphicsExtractor, int mouseX, int mouseY, float partialTick) {
        for (Renderable renderable : renderables) {
            renderable.extractRenderState(GuiGraphicsExtractor, mouseX, mouseY, partialTick);
        }
    }

    protected void drawTitle(GuiGraphicsExtractor GuiGraphicsExtractor) {
        int xCenter = this.width / 2;
        int yOffset = 12;
        GuiGraphicsExtractor.centeredText(this.font, this.title, xCenter, yOffset, -1);
    }
}
