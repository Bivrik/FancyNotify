package net.bivrik.fancynotify;

import net.bivrik.fancynotify.core.FancyNotify;
import net.bivrik.fancynotify.core.Constants;
import net.bivrik.fancynotify.gui.NeoForgeConfigScreen;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class NeoForgePlatform {
    public NeoForgePlatform(IEventBus eventBus) {
        FancyNotify.getInstance().onModInit();

        eventBus.addListener(NeoForgeConfigScreen::registerConfigScreen);
    }
}
