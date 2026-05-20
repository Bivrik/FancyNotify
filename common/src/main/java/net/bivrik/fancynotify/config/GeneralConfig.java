package net.bivrik.fancynotify.config;

public class GeneralConfig extends Config {
    private static final String GENERAL_CONFIG_PATH = ConfigManager.CONFIG_FOLDER_PATH + "general.json";

    public GeneralConfig() {
        super(GENERAL_CONFIG_PATH);
    }

    public Setting<Float> notificationsTransparency = new Setting<>(1.0f);

    @Override
    public String toString() {
        return super.toString().replace("}", ", ") + String.format(
                "notificationTransparency='%s'}",
                notificationsTransparency);
    }
}
