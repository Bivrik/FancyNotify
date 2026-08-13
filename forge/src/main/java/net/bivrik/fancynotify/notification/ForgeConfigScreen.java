package net.bivrik.fancynotify.notification;

import net.bivrik.fancynotify.core.ConfigScreenProvider;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ForgeConfigScreen {
    public static void registerConfigScreen(FMLJavaModLoadingContext context) {
        context.registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(ConfigScreenProvider::getScreen)
        );
    }
}
