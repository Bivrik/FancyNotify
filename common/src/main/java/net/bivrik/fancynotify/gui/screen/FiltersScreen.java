package net.bivrik.fancynotify.gui.screen;

import net.bivrik.fancynotify.FancyNotify;
import net.bivrik.fancynotify.config.ConfigManager;
import net.bivrik.fancynotify.config.Setting;
import net.bivrik.fancynotify.config.data.FiltersConfig;
import net.bivrik.fancynotify.gui.SettingsList;
import net.bivrik.fancynotify.platform.Services;
import net.bivrik.fancynotify.utility.Components;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class FiltersScreen extends UniversalScreen {
    private static final Component TITLE = Components.of("title.filters");
    private static final Component ADVANCEMENTS_NOTIFICATION_TITLE = Components.of("label.advancements");
    private static final Component RECIPES_NOTIFICATION_TITLE = Components.of("label.recipes");
    private static final Component MUSIC_NOTIFICATION_TITLE = Components.of("label.musics");
    private static final Component SCREENSHOT_NOTIFICATION_TITLE = Components.of("label.screenshots");
    private static final Component SYSTEM_NOTIFICATION_TITLE = Components.of("label.systems");
    private static final Component WEATHER_NOTIFICATION_TITLE = Components.of("label.weathers");
    private static final Component BIOME_NOTIFICATION_TITLE = Components.of("label.biomes");
    private static final Component PLAYER_LOGIN_NOTIFICATION_TITLE = Components.of("label.player_logins");
    private static final Component PLAYER_LOGIN_NOTIFICATION_TOOLTIP = Components.of("tooltip.player_logins");
    private static final Component FIELD_GUIDE_NOTIFICATION_TITLE = Components.of("label.field_guide.notifications");
    private static final Component SPECTRUM_MESSAGE_NOTIFICATION_TITLE = Components.of("label.spectrum.messages");
    private static final Component SPECTRUM_REVELATION_NOTIFICATION_TITLE = Components.of("label.spectrum.revelations");
    private static final Component SPECTRUM_UNLOCKED_RECIPE_NOTIFICATION_TITLE = Components.of("label.spectrum.unlocked_recipes");

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
    private CycleButton<Boolean> fieldGuideButton;
    private CycleButton<Boolean> spectrumMessageButton;
    private CycleButton<Boolean> spectrumRevelationButton;
    private CycleButton<Boolean> spectrumUnlockedRecipeButton;

    protected FiltersScreen(Screen parent) {
        super(TITLE, parent);

        this.configManager = FancyNotify.getInstance().getConfigManager();
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

        if (Services.PLATFORM.isModLoaded("fieldguide")) {
            fieldGuideButton = createCycleButton(configManager.getFiltersConfig().isFieldGuideNotificationEnabled, FIELD_GUIDE_NOTIFICATION_TITLE);
            list.addElement(fieldGuideButton);
        }

        if (Services.PLATFORM.isModLoaded("spectrum")) {
            spectrumMessageButton = createCycleButton(configManager.getFiltersConfig().isSpectrumMessageNotificationEnabled, SPECTRUM_MESSAGE_NOTIFICATION_TITLE);
            list.addElement(spectrumMessageButton);
            spectrumRevelationButton = createCycleButton(configManager.getFiltersConfig().isSpectrumRevelationNotificationEnabled, SPECTRUM_REVELATION_NOTIFICATION_TITLE);
            list.addElement(spectrumRevelationButton);
            spectrumUnlockedRecipeButton = createCycleButton(configManager.getFiltersConfig().isSpectrumUnlockedRecipeNotificationEnabled, SPECTRUM_UNLOCKED_RECIPE_NOTIFICATION_TITLE);
            list.addElement(spectrumUnlockedRecipeButton);
        }

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
