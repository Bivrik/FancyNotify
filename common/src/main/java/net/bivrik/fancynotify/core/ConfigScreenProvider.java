package net.bivrik.fancynotify.core;

import net.bivrik.fancynotify.screen.FancyNotifyScreen;
import net.minecraft.client.gui.screens.Screen;

/**
 * Utility class for easier providing of config screen to mod loaders.
 */
public final class ConfigScreenProvider {
    private ConfigScreenProvider() {}

    public static Screen getScreen(Screen parent) {
        return new FancyNotifyScreen(parent);
    }
}
