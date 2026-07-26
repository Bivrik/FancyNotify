package net.bivrik.fancynotify.config;

import net.minecraft.network.chat.Component;

public class GeneralConfig extends Config {
    private static final String GENERAL_CONFIG_PATH = ConfigManager.CONFIG_FOLDER_PATH + "general.json";

    public GeneralConfig() {
        super(GENERAL_CONFIG_PATH);
    }

    public Setting<Float> notificationsTransparency = new Setting<>(1.0f);
    public Setting<Integer> padding = new Setting<>(2);
    public Setting<Integer> maxAmount = new Setting<>(4);
    public Setting<Integer> notificationsWidth = new Setting<>(160);
    public Setting<Orientation> orientation = new Setting<>(Orientation.VERTICAL);
    public Setting<Anchor> anchor = new Setting<>(Anchor.TOP_RIGHT);
    public Setting<Boolean> debug = new Setting<>(false);

    public enum Orientation {
        VERTICAL("vertical"),
        HORIZONTAL("horizontal");

        private final Component displayName;

        Orientation(String name) {
            this.displayName = Component.translatable("fancynotify.gui.orientation." + name);
        }

        public Component getDisplayName() {
            return displayName;
        }
    }

    public enum Anchor {
        TOP_LEFT("top_left", true, true),
        TOP_RIGHT("top_right", false, true),
        BOTTOM_LEFT("bottom_left", true, false),
        BOTTOM_RIGHT("bottom_right", false, false);

        private final Component displayName;
        private final boolean isLeft;
        private final boolean isTop;

        Anchor(String name, boolean isLeft, boolean isTop) {
            this.displayName = Component.translatable("fancynotify.gui.anchor." + name);
            this.isLeft = isLeft;
            this.isTop = isTop;
        }

        public Component getDisplayName() {
            return displayName;
        }

        public boolean isLeft() {
            return isLeft;
        }

        public boolean isTop() {
            return isTop;
        }
    }

    @Override
    public String toString() {
        return super.toString().replace("}", ", ") + String.format(
                "notificationsTransparency='%s', notificationsWidth='%s', anchor='%s'}",
                notificationsTransparency, notificationsWidth, anchor);
    }
}
