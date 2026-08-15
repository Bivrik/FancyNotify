package net.bivrik.fancynotify;

import net.bivrik.fancynotify.core.Constants;
import net.bivrik.fancynotify.utility.Identifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public enum WeatherType {
    CLEAR("clear"),
    RAIN("rain"),
    THUNDER("thunder");

    private final Component displayName;
    private final Identifier icon;

    WeatherType(String name) {
        this.displayName = Component.translatable(Constants.MOD_ID + ".gui.weather." + name);
        this.icon = Identifiers.of("icons/" + name);
    }

    public Component getDisplayName() {
        return displayName;
    }

    public Identifier getIcon() {
        return icon;
    }
}
