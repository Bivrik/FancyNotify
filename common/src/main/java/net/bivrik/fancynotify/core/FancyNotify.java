package net.bivrik.fancynotify.core;

import net.bivrik.fancynotify.BiomeManager;
import net.bivrik.fancynotify.NotificationManager;
import net.bivrik.fancynotify.SplashesManager;
import net.bivrik.fancynotify.config.ConfigManager;
import net.bivrik.fancynotify.credits.CreditsManager;
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
    private Particle2DEngine particleEngine;
    private CreditsManager creditsManager;

    private boolean isInitialized = false;
    private boolean isMinecraftInitialized = false;

    public static FancyNotify getInstance() {
        return INSTANCE;
    }

    public void onModInit() {
        if (isInitialized) {
            Log.warn(Constants.MOD_NAME + " is already initialized!");
            return;
        }
        isInitialized = true;
        Log.info(Constants.MOD_NAME + " initialized on {} ({})", Services.PLATFORM.getName(), Services.PLATFORM.getEnvironmentName());

        configManager = new ConfigManager();
        particleEngine = new Particle2DEngine();
        creditsManager = new CreditsManager();
    }

    public void onMinecraftInit(Minecraft minecraft) {
        if (isMinecraftInitialized) {
            Log.warn("Minecraft is already initialized!");
            return;
        }
        isMinecraftInitialized = true;
        Log.info("Minecraft initialized");

        notificationManager = new NotificationManager(minecraft, configManager);
        splashesManager = new SplashesManager(minecraft);
        biomeManager = new BiomeManager(minecraft, notificationManager);
    }

    public void onClientTick() {
        biomeManager.tick();
        particleEngine.tick();
    }

    public void onGuiRender(GuiGraphics guiGraphics, float partialTick) {
        particleEngine.render(guiGraphics, partialTick);
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

    public Particle2DEngine getParticleEngine() {
        return particleEngine;
    }

    public CreditsManager getCreditsManager() {
        return creditsManager;
    }
}
