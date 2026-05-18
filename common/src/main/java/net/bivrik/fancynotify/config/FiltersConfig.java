package net.bivrik.fancynotify.config;

public class FiltersConfig extends Config {
    public FiltersConfig(String path) {
        super(path);
    }

    public Setting<Boolean> isAdvancementNotificationEnabled = new Setting<>(true);
    public Setting<Boolean> isRecipeNotificationEnabled = new Setting<>(true);
    public Setting<Boolean> isMusicNotificationEnabled = new Setting<>(false);
    public Setting<Boolean> isScreenshotNotificationEnabled = new Setting<>(true);
    public Setting<Boolean> isSystemNotificationEnabled = new Setting<>(true);
    public Setting<Boolean> isWeatherNotificationEnabled = new Setting<>(false);
    public Setting<Boolean> isBiomeNotificationEnabled = new Setting<>(false);

    @Override
    public String toString() {
        return super.toString().replace("}", ", ") + String.format(
                "isAdvancementNotificationEnabled='%s', isRecipieNotificationEnabled='%s', isMusicNotificationEnabled='%s', isScreenshotNotificationEnabled='%s', isSystemNotificationEnabled='%s', isWeatherNotificationEnabled='%s', isBiomeNotificationEnabled='%s'}",
                isAdvancementNotificationEnabled, isRecipeNotificationEnabled, isMusicNotificationEnabled, isScreenshotNotificationEnabled, isSystemNotificationEnabled, isWeatherNotificationEnabled, isBiomeNotificationEnabled);
    }
}
