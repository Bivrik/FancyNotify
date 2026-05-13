package net.bivrik.fancynotify;

import net.bivrik.fancynotify.core.Common;
import net.fabricmc.api.ModInitializer;

public class FabricPlatform implements ModInitializer {
    @Override
    public void onInitialize() {
        Common.onModInit();
    }
}
