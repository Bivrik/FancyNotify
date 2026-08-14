package net.bivrik.fancynotify;

import net.bivrik.fancynotify.core.Constants;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public enum WeatherType {
    CLEAR("clear"),
    RAIN("rain"),
    THUNDER("thunder");

    private final Component displayName;
    private final ResourceLocation icon;

    WeatherType(String name) {
        this.displayName = Component.translatable(Constants.MOD_ID + ".gui.weather." + name);
        this.icon = ResourceLocations.of("icons/" + name);
    }

    public Component getDisplayName() {
        return displayName;
    }

    public ResourceLocation getIcon() {
        return icon;
    }
}
