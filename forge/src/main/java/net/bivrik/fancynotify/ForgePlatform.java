package net.bivrik.fancynotify;

import net.bivrik.fancynotify.core.Common;
import net.bivrik.fancynotify.core.Constants;
import net.bivrik.fancynotify.gui.ForgeConfigScreen;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class ForgePlatform {
    public ForgePlatform(FMLJavaModLoadingContext context) {
        Common.onModInit();

        ForgeConfigScreen.registerConfigScreen(context);
    }
}
