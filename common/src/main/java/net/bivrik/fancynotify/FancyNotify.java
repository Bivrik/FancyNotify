package net.bivrik.fancynotify;

import net.bivrik.fancynotify.config.ConfigManager;
import net.bivrik.fancynotify.core.Constants;
import net.bivrik.fancynotify.core.Log;
import net.bivrik.fancynotify.credits.CreditsManager;
import net.bivrik.fancynotify.eventbus.EventBus;
import net.bivrik.fancynotify.eventbus.IEventBus;
import net.bivrik.fancynotify.notification.NotificationManager;
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
    private WeatherManager weatherManager;
    private MusicManager musicManager;

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
        creditsManager = new CreditsManager();
    }

    public void onMinecraftInit(Minecraft minecraft) {
        if (isMinecraftInitialized) {
            Log.warn("Minecraft is already initialized!");
            return;
        }
        isMinecraftInitialized = true;
        Log.info("Minecraft initialized");

        particleEngine = new Particle2DEngine(minecraft.options, configManager);
        splashesManager = new SplashesManager(minecraft);
        notificationManager = new NotificationManager(minecraft, configManager, particleEngine);
        weatherManager = new WeatherManager(notificationManager);
        biomeManager = new BiomeManager(minecraft, notificationManager);
        musicManager = new MusicManager(minecraft.options, notificationManager);
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

    public WeatherManager getWeatherManager() {
        return weatherManager;
    }

    public MusicManager getMusicManager() {
        return musicManager;
    }
}
