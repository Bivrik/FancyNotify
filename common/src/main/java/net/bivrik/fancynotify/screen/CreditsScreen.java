package net.bivrik.fancynotify.screen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class CreditsScreen extends UniversalScreen {
    private static final Component TITLE = Component.literal("Credits");

    protected CreditsScreen(Screen parent) {
        super(TITLE, parent);
    }
}
