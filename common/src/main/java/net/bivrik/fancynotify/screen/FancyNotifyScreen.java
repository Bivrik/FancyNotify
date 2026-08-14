package net.bivrik.fancynotify.screen;

import net.bivrik.fancynotify.FancyNotify;
import net.bivrik.fancynotify.core.Constants;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class FancyNotifyScreen extends UniversalScreen {
    private static final Component TITLE = Component.literal(Constants.MOD_NAME);
    private static final Component SETTINGS_LABEL = Component.translatable("fancynotify.label.settings");
    private static final Component FILTERS_LABEL = Component.translatable("fancynotify.label.filters");
    private static final Component CREDITS_LABEL = Component.translatable("fancynotify.label.credits");
    private static final Component GITHUB_LABEL = Component.translatable("fancynotify.label.creator_note");
    private static final Component DISCORD_TOOLTIP = Component.translatable("fancynotify.tooltip.discord");
    private static final Component BOOSTY_TOOLTIP = Component.translatable("fancynotify.tooltip.boosty");
    private static final Component YOUTUBE_TOOLTIP = Component.translatable("fancynotify.tooltip.youtube");
    private static final URI GITHUB_URI = URI.create("https://github.com/Bivrik");
    private static final URI DISCORD_URI = URI.create("https://discord.gg/9XuRDgbbZe");
    private static final URI BOOSTY_URI = URI.create("https://boosty.to/bivrik");
    private static final URI YOUTUBE_URI = URI.create("https://www.youtube.com/@modsEnjoyer");

    private final String splash;

    private Button backButton;
    private Button settingsButton;
    private Button filtersButton;
    private Button creditsButton;
    private PlainTextButton supportButton;
    private ImageButton discordButton;
    private ImageButton boostyButton;
    private ImageButton youtubeButton;

    public FancyNotifyScreen(Screen parent) {
        super(TITLE, parent);

        this.splash = FancyNotify.getInstance().getSplashesManager().getSplash();
    }

    @Override
    protected void init() {
        backButton = Button.builder(CommonComponents.GUI_BACK, button -> setScreen(parent)).bounds(this.width / 2 - Button.BIG_WIDTH / 2, this.height / 2 + 4 + Button.DEFAULT_HEIGHT + 8, Button.BIG_WIDTH, Button.DEFAULT_HEIGHT).build();
        addSimpleWidget(backButton);

        settingsButton = Button.builder(SETTINGS_LABEL, button -> setScreen(new SettingsScreen(this))).bounds(this.width / 2 - Button.BIG_WIDTH / 2, this.height / 2 - Button.DEFAULT_HEIGHT - 4, Button.BIG_WIDTH, Button.DEFAULT_HEIGHT).build();
        addSimpleWidget(settingsButton);

        filtersButton = Button.builder(FILTERS_LABEL, button -> setScreen(new FiltersScreen(this))).bounds(this.width / 2 - 100, this.height / 2 + 4, 96, Button.DEFAULT_HEIGHT).build();
        addSimpleWidget(filtersButton);

        creditsButton = Button.builder(CREDITS_LABEL, button -> setScreen(new CreditsScreen(this))).bounds(this.width / 2 + 4, this.height / 2 + 4, 96, Button.DEFAULT_HEIGHT).build();
        addSimpleWidget(creditsButton);

        List<ImageButton> linkButtons = new ArrayList<>();

        discordButton = createLinkButton(18, 18, "links/discord", "links/discord_hover",
                DISCORD_URI, DISCORD_TOOLTIP);
        linkButtons.add(discordButton);

        boostyButton = createLinkButton(18, 18, "links/boosty", "links/boosty_hover",
                BOOSTY_URI, BOOSTY_TOOLTIP);
        linkButtons.add(boostyButton);

        youtubeButton = createLinkButton(18, 18, "links/youtube", "links/youtube_hover",
                YOUTUBE_URI, YOUTUBE_TOOLTIP);
        linkButtons.add(youtubeButton);

        final int padding = 5;
        int x = this.width / 2 + Button.BIG_WIDTH / 2 + padding;
        int y = this.height / 2 - 24;
        for (ImageButton button : linkButtons) {
            button.setPosition(x, y);
            addSimpleWidget(button);
            y += button.getHeight() + padding;
        }

        int supportButtonWidth = this.font.width(GITHUB_LABEL);
        Button.OnPress openGithubAction = ConfirmLinkScreen.confirmLink(this, GITHUB_URI);
        supportButton = new PlainTextButton(this.width - supportButtonWidth - 2, this.height - 9 - 1, supportButtonWidth, 9, GITHUB_LABEL, openGithubAction, this.font);
        addSimpleWidget(supportButton);
    }

    private ImageButton createLinkButton(int width, int height, String icon, String iconHovered, URI link, Component tooltip) {
        Button.OnPress action = ConfirmLinkScreen.confirmLink(this, link);
        ImageButton button = new ImageButton(0, 0, width, height, new WidgetSprites(ResourceLocations.of(icon), ResourceLocations.of(iconHovered)), action);
        button.setTooltip(Tooltip.create(tooltip));
        return button;
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
