package net.bivrik.fancynotify.credits;

import net.bivrik.fancynotify.core.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CreditsList extends AbstractSelectionList<CreditsList.Entry> {
    private final List<Entry> lines = new ArrayList<>(10);
    private float scrollSpeed = 0.4f;

    public CreditsList(Minecraft minecraft, int width, int height, int y, CreditsManager.CreditsData data) {
        super(minecraft, width, height, y, y + height, 18);

        setRenderTopAndBottom(false);
        setRenderBackground(false);

        updateList(data);
    }

    @Override
    protected int getScrollbarPosition() {
        return this.width * 2;
    }

    private void updateList(CreditsManager.CreditsData data) {
        for (int i = 0; i < this.height / this.itemHeight + 2; i++) {
            addSpace();
        }

        for (String category : data.getCategories().keySet()) {
            if (!data.getCategories().get(category).isEmpty()) {
                addCategory(category);
                for (CreditsManager.CreditsData.User user : data.getCategories().get(category)) {
                    addLine(user);
                }
                addSpace();
            }
        }

        for (int i = 0; i < this.height / this.itemHeight; i++) {
            addSpace();
        }

        acceptLines();
    }

    private void acceptLines() {
        for (var line : lines) {
            this.addEntry(line);
        }
    }

    private void addSpace() {
        lines.add(new SpaceEntry(this, ""));
    }

    private void addCategory(String category) {
        lines.add(new CategoryEntry(this, category));
    }

    private void addLine(CreditsManager.CreditsData.User user) {
        String content = user.name().compareTo("{user.name}") != 0 ? user.name() : this.minecraft.getUser().getName();
        lines.add(new UserEntry(this, content, user.annotation()));
    }

    public void scroll() {
        this.setScrollAmount(getScrollAmount() + scrollSpeed);

        if (getScrollAmount() == getMaxScroll()) {
            setScrollAmount(0);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_SPACE) {
            scrollSpeed = 1.2f;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_SPACE) {
            scrollSpeed = 0.4f;
        }

        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput narrationElementOutput) {}

    protected abstract static class Entry extends AbstractSelectionList.Entry<Entry> {
        protected final CreditsList parentList;
        protected final String content;
        protected final Font font;

        public Entry(CreditsList parentList, String content) {
            this.parentList = parentList;
            this.content = content;
            this.font = this.parentList.minecraft.font;
        }

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean isHovering, float partialTick) {}
    }

    private static class CategoryEntry extends Entry {
        private final Component displayName;
        private final int xCenter;

        public CategoryEntry(CreditsList parentList, String content) {
            super(parentList, content);

            this.displayName = Component.translatable(Constants.MOD_ID + ".credits." + this.content);
            this.xCenter = parentList.width / 2;
        }

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean isHovering, float partialTick) {
            guiGraphics.drawCenteredString(font, displayName, xCenter, y, Color.yellow.getRGB());
        }
    }

    private static class SpaceEntry extends Entry {
        public SpaceEntry(CreditsList parentList, String content) {
            super(parentList, content);
        }
    }

    private static class UserEntry extends Entry {
        private final String annotation;
        private final boolean isValidAnnotation;

        public UserEntry(CreditsList parentList, String content, String annotation) {
            super(parentList, content);
            this.annotation = annotation;
            this.isValidAnnotation = this.annotation != null && !this.annotation.isEmpty();
        }

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean isHovering, float partialTick) {
            guiGraphics.drawString(this.font, this.content, x, y, Color.white.getRGB());

            if (isValidAnnotation) {
                guiGraphics.drawString(this.font, annotation, x + font.width(this.content) + 8, y, Color.lightGray.getRGB());
            }
        }
    }
}
