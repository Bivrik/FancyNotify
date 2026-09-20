package net.bivrik.fancynotify;

import net.bivrik.fancynotify.core.ConfigScreenProvider;
import net.bivrik.fancynotify.core.Constants;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(Constants.MOD_ID)
public class NeoForgePlatform {
    public NeoForgePlatform(IEventBus eventBus) {
        FancyNotify.getInstance().onModInit();

        eventBus.addListener(this::registerConfigScreen);
    }

    private void registerConfigScreen(FMLClientSetupEvent event) {
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (modContainer, screen) -> ConfigScreenProvider.getScreen(screen)
        );
    }
}
