package net.bivrik.fancynotify.screen;

import net.bivrik.fancynotify.core.Common;
import net.bivrik.fancynotify.core.Constants;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.net.URI;

public class FancyNotifyScreen extends UniversalScreen {
    private static final Component TITLE = Component.literal(Constants.MOD_NAME);
    private static final Component SUPPORT_LABEL = Component.literal("News, polls, and other stuff on my Boosty blog!");
    private static final URI BOOSTY_URI = URI.create("https://boosty.to/bivrik");

    private final String splash;

    private Button backButton;
    private Button settingsButton;
    private Button filtersButton;
    private Button creditsButton;
    private PlainTextButton supportButton;

    public FancyNotifyScreen(Screen parent) {
        super(TITLE, parent);

        this.splash = Common.getSplashesManager().getSplash();
    }

    @Override
    protected void init() {
        backButton = Button.builder(CommonComponents.GUI_BACK, button -> setScreen(parent)).bounds(this.width / 2 - Button.BIG_WIDTH / 2, this.height / 2 + 4 + Button.DEFAULT_HEIGHT + 8, Button.BIG_WIDTH, Button.DEFAULT_HEIGHT).build();
        this.addSimpleWidget(backButton);

        settingsButton = Button.builder(Component.literal("Settings..."), button -> setScreen(new SettingsScreen(this))).bounds(this.width / 2 - Button.BIG_WIDTH / 2, this.height / 2 - Button.DEFAULT_HEIGHT - 4, Button.BIG_WIDTH, Button.DEFAULT_HEIGHT).build();
        this.addSimpleWidget(settingsButton);

        filtersButton = Button.builder(Component.literal("Filters..."), button -> setScreen(new FiltersScreen(this))).bounds(this.width / 2 - 100, this.height / 2 + 4, 96, Button.DEFAULT_HEIGHT).build();
        this.addSimpleWidget(filtersButton);

        creditsButton = Button.builder(Component.literal("Credits"), button -> setScreen(new CreditsScreen(this))).bounds(this.width / 2 + 4, this.height / 2 + 4, 96, Button.DEFAULT_HEIGHT).build();
        this.addSimpleWidget(creditsButton);

        int supportButtonWidth = this.font.width(SUPPORT_LABEL);
        Button.OnPress supportButtonAction = ConfirmLinkScreen.confirmLink(this, BOOSTY_URI);
        supportButton = new PlainTextButton(this.width - supportButtonWidth - 4, this.height - 13, supportButtonWidth, 9, SUPPORT_LABEL, supportButtonAction, this.font);
        this.addSimpleWidget(supportButton);
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
        guiGraphics.drawCenteredString(this.font, splash, this.width / 2, 12 + 9, Color.yellow.getRGB());
        stack.popPose();
    }
}
