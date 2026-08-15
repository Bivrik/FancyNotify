package net.bivrik.fancynotify.screen;

import net.bivrik.fancynotify.FancyNotify;
import net.bivrik.fancynotify.core.Constants;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FancyNotifyScreen extends UniversalScreen {
    private static final ResourceLocation LINKS = ResourceLocations.of("textures/gui/links.png");
    private static final Component TITLE = Component.literal(Constants.MOD_NAME);
    private static final Component SETTINGS_LABEL = Component.translatable("fancynotify.label.settings");
    private static final Component FILTERS_LABEL = Component.translatable("fancynotify.label.filters");
    private static final Component CREDITS_LABEL = Component.translatable("fancynotify.label.credits");
    private static final Component GITHUB_LABEL = Component.translatable("fancynotify.label.creator_note");
    private static final Component DISCORD_TOOLTIP = Component.translatable("fancynotify.tooltip.discord");
    private static final Component BOOSTY_TOOLTIP = Component.translatable("fancynotify.tooltip.boosty");
    private static final Component YOUTUBE_TOOLTIP = Component.translatable("fancynotify.tooltip.youtube");
    private static final String GITHUB_LINK = "https://github.com/Bivrik";
    private static final String DISCORD_LINK = "https://discord.gg/9XuRDgbbZe";
    private static final String BOOSTY_LINK = "https://boosty.to/bivrik";
    private static final String YOUTUBE_LINK = "https://www.youtube.com/@modsEnjoyer";

    private final String splash;

    private Button backButton;
    private Button settingsButton;
    private Button filtersButton;
    private Button creditsButton;
    private PlainTextButton supportButton;
    private LinkButton discordButton;
    private LinkButton boostyButton;
    private LinkButton youtubeButton;

    public FancyNotifyScreen(Screen parent) {
        super(TITLE, parent);

        this.splash = FancyNotify.getInstance().getSplashesManager().getSplash();
    }

    @Override
    protected void init() {
        backButton = Button.builder(CommonComponents.GUI_BACK, button -> setScreen(parent)).bounds(this.width / 2 - 200 / 2, this.height / 2 + 4 + Button.DEFAULT_HEIGHT + 8, 200, Button.DEFAULT_HEIGHT).build();
        addSimpleWidget(backButton);

        settingsButton = Button.builder(SETTINGS_LABEL, button -> setScreen(new SettingsScreen(this))).bounds(this.width / 2 - 200 / 2, this.height / 2 - Button.DEFAULT_HEIGHT - 4, 200, Button.DEFAULT_HEIGHT).build();
        addSimpleWidget(settingsButton);

        filtersButton = Button.builder(FILTERS_LABEL, button -> setScreen(new FiltersScreen(this))).bounds(this.width / 2 - 100, this.height / 2 + 4, 96, Button.DEFAULT_HEIGHT).build();
        addSimpleWidget(filtersButton);

        creditsButton = Button.builder(CREDITS_LABEL, button -> setScreen(new CreditsScreen(this))).bounds(this.width / 2 + 4, this.height / 2 + 4, 96, Button.DEFAULT_HEIGHT).build();
        addSimpleWidget(creditsButton);

        List<LinkButton> linkButtons = new ArrayList<>();

        discordButton = createLinkButton(DISCORD_LINK, DISCORD_TOOLTIP, new UV(0, 18), new UV(18, 18));
        linkButtons.add(discordButton);

        boostyButton = createLinkButton(BOOSTY_LINK, BOOSTY_TOOLTIP, new UV(0, 0), new UV(18, 0));
        linkButtons.add(boostyButton);

        youtubeButton = createLinkButton(YOUTUBE_LINK, YOUTUBE_TOOLTIP, new UV(0, 36), new UV(18, 36));
        linkButtons.add(youtubeButton);

        final int padding = 5;
        int x = this.width / 2 + 200 / 2 + padding;
        int y = this.height / 2 - 24;
        for (LinkButton button : linkButtons) {
            button.setPosition(x, y);
            addSimpleWidget(button);
            y += button.getHeight() + padding;
        }

        int supportButtonWidth = this.font.width(GITHUB_LABEL);
        Button.OnPress openGithubAction = ConfirmLinkScreen.confirmLink(GITHUB_LINK, this, true);
        supportButton = new PlainTextButton(this.width - supportButtonWidth - 2, this.height - 9 - 1, supportButtonWidth, 9, GITHUB_LABEL, openGithubAction, this.font);
        addSimpleWidget(supportButton);
    }

    private LinkButton createLinkButton(String link, Component tooltip, UV iconUv, UV iconHoveredUv) {
        Button.OnPress action = ConfirmLinkScreen.confirmLink(link, this, true);
        LinkButton button = new LinkButton(LINKS, action, iconUv, iconHoveredUv);
        button.setTooltip(Tooltip.create(tooltip));
        return button;
    }

    private static class LinkButton extends Button {
        private final ResourceLocation sprites;
        private final UV iconUv;
        private final UV iconHoveredUv;

        public LinkButton(ResourceLocation sprites, OnPress onPress, UV iconUv, UV iconHoveredUv) {
            super(0, 0, 18, 18, Component.empty(), onPress, DEFAULT_NARRATION);

            this.sprites = sprites;
            this.iconUv = iconUv;
            this.iconHoveredUv = iconHoveredUv;
        }

        @Override
        protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            UV uv;
            if (isHoveredOrFocused()) {
                uv = iconHoveredUv;
            } else {
                uv = iconUv;
            }

            guiGraphics.blit(sprites, getX(), getY(), uv.uOffset, uv.vOffset, getWidth(), getHeight(), 64, 64);
        }
    }

    private record UV(int uOffset, int vOffset) {}

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
