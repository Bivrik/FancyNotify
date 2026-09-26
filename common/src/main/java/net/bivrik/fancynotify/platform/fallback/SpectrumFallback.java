package net.bivrik.fancynotify.platform.fallback;

import net.bivrik.fancynotify.api.NotificationManager;
import net.bivrik.fancynotify.platform.api.SpectrumApi;
import net.minecraft.client.gui.components.toasts.Toast;

public class SpectrumFallback implements SpectrumApi {
    @Override
    public boolean tryHandleMessageToast(Toast toast, NotificationManager notificationManager) {
        return false;
    }

    @Override
    public boolean tryHandleRevelationToast(Toast toast, NotificationManager notificationManager) {
        return false;
    }

    @Override
    public boolean tryHandleUnlockedRecipeToast(Toast toast, NotificationManager notificationManager) {
        return false;
    }
}
