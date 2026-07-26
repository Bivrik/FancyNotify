package net.bivrik.fancynotify.core;

import net.bivrik.fancynotify.BiomeManager;
import net.bivrik.fancynotify.NotificationManager;
import net.bivrik.fancynotify.SplashesManager;
import net.bivrik.fancynotify.config.ConfigManager;
import net.bivrik.fancynotify.gui.SystemNotification;
import net.bivrik.fancynotify.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public final class Common {
    private Common() {}

    private static ConfigManager configManager;
    private static NotificationManager notificationManager;
    private static SplashesManager splashesManager;
    private static BiomeManager biomeManager;

    public static void onModInit() {
        if (!Services.PLATFORM.isModLoaded(Constants.MOD_ID)) {
            return;
        }
        Log.info("Initialized on {} in a {} environment", Services.PLATFORM.getName(), Services.PLATFORM.getEnvironmentName());
    }

    public static void onMinecraftInit(Minecraft minecraft) {
        configManager = new ConfigManager();
        notificationManager = new NotificationManager(minecraft, configManager);
        splashesManager = new SplashesManager(minecraft);
        biomeManager = new BiomeManager(minecraft, notificationManager);
    }

    public static void onClientTick() {
        biomeManager.tick();
    }

    public static NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public static SplashesManager getSplashesManager() {
        return splashesManager;
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }
}
