package net.bivrik.fancynotify;

import net.bivrik.fancynotify.api.FancyNotifyApi;
import net.bivrik.fancynotify.api.NotificationManager;
import net.bivrik.fancynotify.biome.BiomeManager;
import net.bivrik.fancynotify.config.ConfigManager;
import net.bivrik.fancynotify.core.Constants;
import net.bivrik.fancynotify.core.Log;
import net.bivrik.fancynotify.eventbus.EventBus;
import net.bivrik.fancynotify.eventbus.EventBusImpl;
import net.bivrik.fancynotify.notification.NotificationEngine;
import net.bivrik.fancynotify.notification.NotificationEngineImpl;
import net.bivrik.fancynotify.particle.Particle2DEngine;
import net.bivrik.fancynotify.platform.Services;
import net.bivrik.fancynotify.weather.WeatherManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import org.jetbrains.annotations.Nullable;

public final class FancyNotify {
    private FancyNotify() {}

    private static final FancyNotify INSTANCE = new FancyNotify();

    public static final EventBus EVENT_BUS = new EventBusImpl();

    private ConfigManager configManager;
    private NotificationEngine notificationEngine;
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
        Services.bootstrap();
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
        notificationEngine = new NotificationEngineImpl(minecraft, configManager);
        notificationManager = notificationEngine;
        FancyNotifyApi.setNotificationManager(notificationManager);
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

    /**
     * Always check for null if called from mixin. No idea why it happens sometimes, but some mods are calling {@link ToastComponent#render(GuiGraphics)} too early somehow
     * @return {@link NotificationEngine}
     */
    public @Nullable NotificationEngine getNotificationEngine() {
        return notificationEngine;
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
