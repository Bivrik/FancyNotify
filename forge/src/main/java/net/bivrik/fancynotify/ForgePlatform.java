package net.bivrik.fancynotify;

import net.bivrik.fancynotify.core.ConfigScreenProvider;
import net.bivrik.fancynotify.core.Constants;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class ForgePlatform {
    public ForgePlatform(FMLJavaModLoadingContext context) {
        FancyNotify.getInstance().onModInit();

        registerConfigScreen(context);
    }

    private void registerConfigScreen(FMLJavaModLoadingContext context) {
        context.registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(ConfigScreenProvider::getScreen)
        );
    }
}
