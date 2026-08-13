package net.bivrik.fancynotify;

import net.fabricmc.api.ModInitializer;

public class FabricPlatform implements ModInitializer {
    @Override
    public void onInitialize() {
        FancyNotify.getInstance().onModInit();
    }
}
