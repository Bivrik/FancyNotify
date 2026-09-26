package net.bivrik.fancynotify.utility;

import net.bivrik.fancynotify.core.Constants;
import net.minecraft.network.chat.Component;

public final class Components {
    private Components() {}

    public static Component of(String path) {
        return Component.translatable(Constants.MOD_ID + "." + path);
    }
}
