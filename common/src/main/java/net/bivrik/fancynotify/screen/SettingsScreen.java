package net.bivrik.fancynotify.screen;

import net.bivrik.fancynotify.config.ConfigManager;
import net.bivrik.fancynotify.config.GeneralConfig;
import net.bivrik.fancynotify.core.Common;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class SettingsScreen extends UniversalScreen {
    private static final Component TITLE = Component.literal("Settings");

    private final ConfigManager configManager;
    private final GeneralConfig generalConfig;

    private Button backButton;

    protected SettingsScreen(Screen parent) {
        super(TITLE, parent);

        this.configManager = Common.getConfigManager();
        this.generalConfig = this.configManager.getGeneralConfig();
    }

    @Override
    protected void init() {
        backButton = Button.builder(CommonComponents.GUI_BACK, button -> {
            configManager.write(generalConfig);
            this.setScreen(parent);
        }).bounds(this.width / 2 - Button.BIG_WIDTH / 2, this.height - Button.DEFAULT_HEIGHT - 16, Button.BIG_WIDTH, Button.DEFAULT_HEIGHT).build();
        this.addSimpleWidget(backButton);

        var d = CycleButton.builder(GeneralConfig.Test::getDisplayName).withInitialValue(generalConfig.testSetting.get()).withValues(GeneralConfig.Test.values()).create(0, 0, Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, Component.literal("Test"), (button, value) -> generalConfig.testSetting.set(value));
        this.addSimpleWidget(d);
    }
}
