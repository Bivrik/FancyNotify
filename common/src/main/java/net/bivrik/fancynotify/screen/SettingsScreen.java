package net.bivrik.fancynotify.screen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class SettingsScreen extends UniversalScreen {
    private static final Component TITLE = Component.literal("Settings");

    protected SettingsScreen(Screen parent) {
        super(TITLE, parent);
    }
}
