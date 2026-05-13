package net.bivrik.fancynotify.core;

import net.bivrik.fancynotify.BiomeManager;
import net.bivrik.fancynotify.NotificationManager;
import net.bivrik.fancynotify.gui.SystemNotification;
import net.bivrik.fancynotify.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public final class Common {
    private Common() {}

    private static NotificationManager notificationManager;
    private static BiomeManager biomeManager;

    public static void onModInit() {
        if (!Services.PLATFORM.isModLoaded(Constants.MOD_ID)) {
            return;
        }
        Logger.info("Initialized on {} in a {} environment", Services.PLATFORM.getName(), Services.PLATFORM.getEnvironmentName());
    }

    public static void onMinecraftInit(Minecraft minecraft) {
        notificationManager = new NotificationManager(minecraft);
        biomeManager = new BiomeManager(minecraft, notificationManager);
    }

    public static void onClientTick() {
        biomeManager.tick();
    }

    // Only for testing
    public static void onKeyPressed(int key) {
        switch (key) {
            case GLFW.GLFW_KEY_1 -> notificationManager.add(new SystemNotification(notificationManager,
                    SystemNotification.Identifier.WORLD_BACKUP, Component.literal("Some title"), Component.literal("Some error message")));
            case GLFW.GLFW_KEY_2 -> notificationManager.add(new SystemNotification(notificationManager,
                    SystemNotification.Identifier.PACK_LOAD_FAILURE, Component.literal("Some title"), Component.literal("Some error message")));
            case GLFW.GLFW_KEY_3 -> notificationManager.add(new SystemNotification(notificationManager,
                    SystemNotification.Identifier.WORLD_ACCESS_FAILURE, Component.literal("Some title"), Component.literal("Some error message")));
            case GLFW.GLFW_KEY_4 -> notificationManager.add(new SystemNotification(notificationManager,
                    SystemNotification.Identifier.PACK_COPY_FAILURE, Component.literal("Some title"), Component.literal("Some error message")));
            case GLFW.GLFW_KEY_5 -> notificationManager.add(new SystemNotification(notificationManager,
                    SystemNotification.Identifier.FILE_DROP_FAILURE, Component.literal("Some title"), Component.literal("Some error message")));
            case GLFW.GLFW_KEY_6 -> notificationManager.add(new SystemNotification(notificationManager,
                    SystemNotification.Identifier.PERIODIC_NOTIFICATION, Component.literal("Some title"), Component.literal("Some error message")));
            case GLFW.GLFW_KEY_7 -> notificationManager.add(new SystemNotification(notificationManager,
                    SystemNotification.Identifier.LOW_DISK_SPACE, Component.literal("Some title"), Component.literal("Some error message")));
            case GLFW.GLFW_KEY_8 -> notificationManager.add(new SystemNotification(notificationManager,
                    SystemNotification.Identifier.CHUNK_LOAD_FAILURE, Component.literal("Some title"), Component.literal("Some error message")));
            case GLFW.GLFW_KEY_9 -> notificationManager.add(new SystemNotification(notificationManager,
                    SystemNotification.Identifier.CHUNK_SAVE_FAILURE, Component.literal("Bivrik is lazy"), Component.literal("WHO WROTE THAT?!")));
            case GLFW.GLFW_KEY_0 -> notificationManager.add(new SystemNotification(notificationManager,
                    SystemNotification.Identifier.UNSECURE_SERVER_WARNING, Component.literal("Some title"), Component.literal("Some error message")));
            case GLFW.GLFW_KEY_Q -> notificationManager.remove(SystemNotification.class, SystemNotification.Identifier.LOW_DISK_SPACE);
        }
    }

    public static NotificationManager getNotificationManager() {
        return notificationManager;
    }
}
