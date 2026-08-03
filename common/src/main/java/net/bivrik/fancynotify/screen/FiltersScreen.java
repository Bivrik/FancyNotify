package net.bivrik.fancynotify.screen;

import net.bivrik.fancynotify.config.ConfigManager;
import net.bivrik.fancynotify.config.FiltersConfig;
import net.bivrik.fancynotify.config.Setting;
import net.bivrik.fancynotify.core.Common;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import javax.tools.Tool;

public class FiltersScreen extends UniversalScreen {
    private static final Component TITLE = Component.literal("Notifications Filter");
    private static final Component ADVANCEMENTS_NOTIFICATION_TITLE = Component.literal("Advancement's");
    private static final Component RECIPES_NOTIFICATION_TITLE = Component.literal("Recipe's");
    private static final Component MUSIC_NOTIFICATION_TITLE = Component.literal("Music's");
    private static final Component SCREENSHOT_NOTIFICATION_TITLE = Component.literal("Screenshot's");
    private static final Component SYSTEM_NOTIFICATION_TITLE = Component.literal("System's");
    private static final Component WEATHER_NOTIFICATION_TITLE = Component.literal("Weather's");
    private static final Component BIOME_NOTIFICATION_TITLE = Component.literal("Biome's");
    private static final Component PLAYER_LOGIN_NOTIFICATION_TITLE = Component.literal("Player Login's");
    private static final Component PLAYER_LOGIN_NOTIFICATION_TOOLTIP = Component.literal("This notification is unstable! Is not recommended on servers with big online.");

    private final ConfigManager configManager;

    private Button backButton;
    private CycleButton<Boolean> advancementsButton;
    private CycleButton<Boolean> recipesButton;
    private CycleButton<Boolean> musicButton;
    private CycleButton<Boolean> screenshotButton;
    private CycleButton<Boolean> systemButton;
    private CycleButton<Boolean> weatherButton;
    private CycleButton<Boolean> biomeButton;
    private CycleButton<Boolean> playerLoginButton;

    protected FiltersScreen(Screen parent) {
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

        advancementsButton = createCycleButton(configManager.getFiltersConfig().isAdvancementNotificationEnabled, ADVANCEMENTS_NOTIFICATION_TITLE);
        list.addElement(advancementsButton);
        recipesButton = createCycleButton(configManager.getFiltersConfig().isRecipeNotificationEnabled, RECIPES_NOTIFICATION_TITLE);
        list.addElement(recipesButton);
        musicButton = createCycleButton(configManager.getFiltersConfig().isMusicNotificationEnabled, MUSIC_NOTIFICATION_TITLE);
        list.addElement(musicButton);
        screenshotButton = createCycleButton(configManager.getFiltersConfig().isScreenshotNotificationEnabled, SCREENSHOT_NOTIFICATION_TITLE);
        list.addElement(screenshotButton);
        systemButton = createCycleButton(configManager.getFiltersConfig().isSystemNotificationEnabled, SYSTEM_NOTIFICATION_TITLE);
        list.addElement(systemButton);
        weatherButton = createCycleButton(configManager.getFiltersConfig().isWeatherNotificationEnabled, WEATHER_NOTIFICATION_TITLE);
        list.addElement(weatherButton);
        biomeButton = createCycleButton(configManager.getFiltersConfig().isBiomeNotificationEnabled, BIOME_NOTIFICATION_TITLE);
        list.addElement(biomeButton);
        playerLoginButton = createCycleButtonWithTooltip(configManager.getFiltersConfig().isLoginPlayerNotificationEnabled, PLAYER_LOGIN_NOTIFICATION_TITLE, PLAYER_LOGIN_NOTIFICATION_TOOLTIP);
        list.addElement(playerLoginButton);

        list.alignElements();
    }

    private CycleButton<Boolean> createCycleButton(Setting<Boolean> setting, Component title) {
        return CycleButton.onOffBuilder(setting.get()).create(0, 0, Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, title, (button, value) -> setting.set(value));
    }

    private CycleButton<Boolean> createCycleButtonWithTooltip(Setting<Boolean> setting, Component title, Component tooltip) {
        CycleButton<Boolean> button = createCycleButton(setting, title);
        button.setTooltip(Tooltip.create(tooltip));
        return button;
    }

    @Override
    public void onClose() {
        configManager.write(FiltersConfig.class);
        super.onClose();
    }
}
