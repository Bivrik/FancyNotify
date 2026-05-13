package net.bivrik.fancynotify.gui;

import net.bivrik.fancynotify.core.ConfigScreenProvider;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public class NeoForgeConfigScreen {
    public static void registerConfigScreen(FMLClientSetupEvent event) {
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (modContainer, screen) -> ConfigScreenProvider.getScreen(screen)
        );
    }
}
