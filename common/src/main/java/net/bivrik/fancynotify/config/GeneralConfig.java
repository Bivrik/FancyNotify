package net.bivrik.fancynotify.config;

import net.minecraft.network.chat.Component;

public class GeneralConfig extends Config {
    public GeneralConfig(String path) {
        super(path);
    }

    public Setting<Float> notificationTransparency = new Setting<>(1.0f);
    public Setting<Test> testSetting = new Setting<>(Test.POMIDOR);

    public enum Test {
        VISIBLE,
        HIDDEN,
        POMIDOR;

        public Component getDisplayName() {
            return Component.literal(this.name().toLowerCase());
        }
    }

    @Override
    public String toString() {
        return super.toString().replace("}", ", ") + String.format(
                "notificationTransparency='%s', testSetting='%s'}",
                notificationTransparency, testSetting);
    }
}
