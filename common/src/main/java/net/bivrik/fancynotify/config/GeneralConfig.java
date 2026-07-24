package net.bivrik.fancynotify.config;

import net.minecraft.network.chat.Component;

public class GeneralConfig extends Config {
    private static final String GENERAL_CONFIG_PATH = ConfigManager.CONFIG_FOLDER_PATH + "general.json";

    public GeneralConfig() {
        super(GENERAL_CONFIG_PATH);
    }

    public Setting<Float> notificationsTransparency = new Setting<>(1.0f);
    public Setting<Integer> notificationsWidth = new Setting<>(150);
    public Setting<Anchor> anchor = new Setting<>(Anchor.TOP_RIGHT);

    public enum Anchor {
        TOP_LEFT("top_left"),
        TOP_RIGHT("top_right"),
        BOTTOM_LEFT("bottom_left"),
        BOTTOM_RIGHT("bottom_right");

        private final Component displayName;

        Anchor(String name) {
            this.displayName = Component.translatable("fancynotify.gui.anchor." + name);
        }

        public Component getDisplayName() {
            return displayName;
        }
    }

    @Override
    public String toString() {
        return super.toString().replace("}", ", ") + String.format(
                "notificationsTransparency='%s', notificationsWidth='%s', anchor='%s'}",
                notificationsTransparency, notificationsWidth, anchor);
    }
}
