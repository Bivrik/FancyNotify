package net.bivrik.fancynotify.config.data;

import net.bivrik.fancynotify.config.ConfigManager;
import net.bivrik.fancynotify.config.Setting;

public class FiltersConfig extends Config {
    private static final String FILTERS_CONFIG_PATH = ConfigManager.CONFIG_FOLDER_PATH + "filters.json";

    public FiltersConfig() {
        super(FILTERS_CONFIG_PATH);
    }

    public Setting<Boolean> isAdvancementNotificationEnabled = new Setting<>(true);
    public Setting<Boolean> isRecipeNotificationEnabled = new Setting<>(true);
    public Setting<Boolean> isMusicNotificationEnabled = new Setting<>(false);
    public Setting<Boolean> isScreenshotNotificationEnabled = new Setting<>(true);
    public Setting<Boolean> isSystemNotificationEnabled = new Setting<>(true);
    public Setting<Boolean> isWeatherNotificationEnabled = new Setting<>(false);
    public Setting<Boolean> isBiomeNotificationEnabled = new Setting<>(false);
    public Setting<Boolean> isLoginPlayerNotificationEnabled = new Setting<>(false);
    public Setting<Boolean> isFieldGuideNotificationEnabled = new Setting<>(true);
    public Setting<Boolean> isSpectrumMessageNotificationEnabled = new Setting<>(true);
    public Setting<Boolean> isSpectrumRevelationNotificationEnabled = new Setting<>(true);
    public Setting<Boolean> isSpectrumUnlockedRecipeNotificationEnabled = new Setting<>(true);

    @Override
    public String toString() {
        return getBaseToStringBuilder()
                .append("isAdvancementNotificationEnabled", isAdvancementNotificationEnabled.get())
                .append("isRecipeNotificationEnabled", isRecipeNotificationEnabled.get())
                .append("isMusicNotificationEnabled", isMusicNotificationEnabled.get())
                .append("isScreenshotNotificationEnabled", isScreenshotNotificationEnabled.get())
                .append("isSystemNotificationEnabled", isSystemNotificationEnabled.get())
                .append("isWeatherNotificationEnabled", isWeatherNotificationEnabled.get())
                .append("isBiomeNotificationEnabled", isBiomeNotificationEnabled.get())
                .append("isLoginPlayerNotificationEnabled", isLoginPlayerNotificationEnabled.get())
                .append("isFieldGuideNotificationEnabled", isFieldGuideNotificationEnabled.get())
                .append("isSpectrumMessageNotificationEnabled", isSpectrumMessageNotificationEnabled.get())
                .append("isSpectrumRevelationNotificationEnabled", isSpectrumRevelationNotificationEnabled.get())
                .append("isSpectrumUnlockedRecipeNotificationEnabled", isSpectrumUnlockedRecipeNotificationEnabled.get())
                .toString();
    }
}
