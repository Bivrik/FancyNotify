package net.bivrik.fancynotify.gui;

import net.bivrik.fancynotify.core.ConfigScreenProvider;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;

public class ForgeConfigScreen {
    public static void registerConfigScreen(ModLoadingContext context) {
        context.registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(ConfigScreenProvider::getScreen)
        );
    }
}
