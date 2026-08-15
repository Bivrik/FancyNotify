package net.bivrik.fancynotify.screen;

import net.minecraft.client.gui.GuiGraphics;
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
        Objects.requireNonNull(this.minecraft).setScreen(screen);
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
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        drawRenderables(guiGraphics, mouseX, mouseY, partialTick);
        drawTitle(guiGraphics);
    }

    protected void drawRenderables(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        for (Renderable renderable : renderables) {
            renderable.render(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    protected void drawTitle(GuiGraphics guiGraphics) {
        int xCenter = this.width / 2;
        int yOffset = 12;
        guiGraphics.drawCenteredString(this.font, this.title, xCenter, yOffset, -1);
    }
}
