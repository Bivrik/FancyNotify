package net.bivrik.fancynotify;

import net.bivrik.fancynotify.api.gui.notification.NotificationManager;
import net.bivrik.fancynotify.core.Log;
import net.bivrik.fancynotify.gui.notification.DayCounterNotification;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class DayCounterManager {
    private static final int UPDATE_DELAY_TICKS = 100;

    private final Minecraft minecraft;
    private final NotificationManager notificationManager;

    private int counterTick;
    private int lastDaySaved;

    public DayCounterManager(Minecraft minecraft, NotificationManager notificationManager) {
        this.minecraft = minecraft;
        this.notificationManager = notificationManager;
    }

    public void tick() {
        if (minecraft.level == null) {
            lastDaySaved = -1;
            return;
        }

        if (counterTick++ > UPDATE_DELAY_TICKS) {
            counterTick = 0;

            int dayCount = (int) Math.floor(minecraft.level.getDayTime() / 24000.0);

            if (dayCount != lastDaySaved) {
                Log.warn("New day: {}", dayCount);
                if (dayCount % 5 == 0) {
                    Log.warn("[!] Daymn {}!", dayCount);
                    notificationManager.add(new DayCounterNotification(dayCount));
                }
            }

            lastDaySaved = dayCount;

            Log.warn("Current: {}", dayCount);
        }
    }
}
