package net.bivrik.fancynotify.screen;

import net.bivrik.fancynotify.core.Constants;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class FancyNotifyScreen extends UniversalScreen {
    private static final Component TITLE = Component.literal(Constants.MOD_NAME);

    private Button backButton;
    private Button settingsButton;
    private Button filtersButton;
    private Button creditsButton;

    public FancyNotifyScreen(Screen parent) {
        super(TITLE, parent);
    }

    @Override
    protected void init() {
        settingsButton = Button.builder(Component.literal("Settings..."), button -> setScreen(new SettingsScreen(this))).bounds(this.width / 2 - Button.BIG_WIDTH / 2, this.height / 2 - Button.DEFAULT_HEIGHT - 4, Button.BIG_WIDTH, Button.DEFAULT_HEIGHT).build();
        addSimpleWidget(settingsButton);

        filtersButton = Button.builder(Component.literal("Filters..."), button -> setScreen(new FiltersScreen(this))).bounds(this.width / 2 - 100, this.height / 2 + 4, 96, Button.DEFAULT_HEIGHT).build();
        addSimpleWidget(filtersButton);

        creditsButton = Button.builder(Component.literal("Credits"), button -> setScreen(new CreditsScreen(this))).bounds(this.width / 2 + 4, this.height / 2 + 4, 96, Button.DEFAULT_HEIGHT).build();
        addSimpleWidget(creditsButton);

        backButton = Button.builder(CommonComponents.GUI_BACK, button -> setScreen(parent)).bounds(this.width / 2 - Button.BIG_WIDTH / 2, this.height - Button.DEFAULT_HEIGHT - 16, Button.BIG_WIDTH, Button.DEFAULT_HEIGHT).build();
        addSimpleWidget(backButton);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        drawSplash(guiGraphics);
    }

    private void drawSplash(@NotNull GuiGraphics guiGraphics) {
        float size = (float) (Math.abs(Math.cos((double) Util.getMillis() / 250) * 0.1f) + 0.9f);
        float x = this.width / 2.0f;
        float y = 12 + 9 + 4.5f;

        var stack = guiGraphics.pose();
        stack.pushPose();
        stack.translate(x, y, 0);
        stack.scale(size, size, 1);
        stack.translate(-x, -y, 0);
        guiGraphics.drawCenteredString(this.font, "Placeholder", this.width / 2, 12 + 9, Color.yellow.getRGB());
        stack.popPose();
    }
}
