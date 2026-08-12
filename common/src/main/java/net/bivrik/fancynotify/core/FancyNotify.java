package net.bivrik.fancynotify.core;

import net.bivrik.fancynotify.BiomeManager;
import net.bivrik.fancynotify.NotificationManager;
import net.bivrik.fancynotify.SplashesManager;
import net.bivrik.fancynotify.config.ConfigManager;
import net.bivrik.fancynotify.eventbus.EventBus;
import net.bivrik.fancynotify.eventbus.IEventBus;
import net.bivrik.fancynotify.particle.Particle2DEngine;
import net.bivrik.fancynotify.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public final class FancyNotify {
    private FancyNotify() {}

    public static final IEventBus EVENT_BUS = new EventBus();
    private static final FancyNotify INSTANCE = new FancyNotify();

    private ConfigManager configManager;
    private NotificationManager notificationManager;
    private SplashesManager splashesManager;
    private BiomeManager biomeManager;
    private Particle2DEngine particle2DEngine;

    private boolean isMinecraftInitialized = false;

    public static FancyNotify getInstance() {
        return INSTANCE;
    }

    public void onModInit() {
        Log.info("Initialized on {} in a {} environment", Services.PLATFORM.getName(), Services.PLATFORM.getEnvironmentName());
    }

    public void onMinecraftInit(Minecraft minecraft) {
        if (isMinecraftInitialized) {
            Log.warn("Minecraft is already initialized!");
            return;
        }
        isMinecraftInitialized = true;
        Log.info("Minecraft initialized");

        configManager = new ConfigManager();
        particle2DEngine = new Particle2DEngine();
        notificationManager = new NotificationManager(minecraft, configManager);
        splashesManager = new SplashesManager(minecraft);
        biomeManager = new BiomeManager(minecraft, notificationManager);
    }

    public void onClientTick() {
        biomeManager.tick();
        particle2DEngine.tick();
    }

    public void onGuiRender(GuiGraphics guiGraphics, float partialTick) {
        particle2DEngine.render(guiGraphics, partialTick);
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public SplashesManager getSplashesManager() {
        return splashesManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public Particle2DEngine getParticle2DEngine() {
        return particle2DEngine;
    }
}
