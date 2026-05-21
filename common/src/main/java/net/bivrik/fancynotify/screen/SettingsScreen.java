package net.bivrik.fancynotify.screen;

import net.bivrik.fancynotify.Slider;
import net.bivrik.fancynotify.config.ConfigManager;
import net.bivrik.fancynotify.config.GeneralConfig;
import net.bivrik.fancynotify.core.Common;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class SettingsScreen extends UniversalScreen {
    private static final Component TITLE = Component.literal("Settings");
    private static final Component TRANSPARENCY_TITLE = Component.literal("Notifications Transparency");

    private final ConfigManager configManager;

    private Button backButton;
    private Slider transparencySlider;

    protected SettingsScreen(Screen parent) {
        super(TITLE, parent);

        this.configManager = Common.getConfigManager();
    }

    @Override
    protected void init() {
        backButton = Button.builder(CommonComponents.GUI_BACK, button -> this.onClose())
                .bounds(this.width / 2 - Button.BIG_WIDTH / 2, this.height - Button.DEFAULT_HEIGHT - 6, Button.BIG_WIDTH, Button.DEFAULT_HEIGHT).build();
        this.addSimpleWidget(backButton);

        SettingsList list = new SettingsList(this.minecraft, this.width, this.height - 64 - 2, 32, 25, this);
        this.addSimpleWidget(list);

        transparencySlider = new Slider(0, 0, SettingsList.WidgetWidth.BIG.getWidth(), Button.DEFAULT_HEIGHT, TRANSPARENCY_TITLE, configManager.getGeneralConfig().notificationsTransparency.get(), 1.0f);
        transparencySlider.setDisplayer(value -> Component.literal(Math.round(value * 100) + "%"));
        transparencySlider.setResponder(value -> configManager.getGeneralConfig().notificationsTransparency.set(value));
        list.addElement(transparencySlider, SettingsList.WidgetWidth.BIG);

        list.alignElements();
    }

    @Override
    public void onClose() {
        configManager.write(GeneralConfig.class);
        super.onClose();
    }
}
