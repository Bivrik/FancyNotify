package net.bivrik.fancynotify.screen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class FiltersScreen extends UniversalScreen {
    private static final Component TITLE = Component.literal("Filters");

    protected FiltersScreen(Screen parent) {
        super(TITLE, parent);
    }
}
