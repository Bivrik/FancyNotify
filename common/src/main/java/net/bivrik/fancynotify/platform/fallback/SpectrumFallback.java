package net.bivrik.fancynotify.platform.fallback;

import net.bivrik.fancynotify.api.NotificationManager;
import net.bivrik.fancynotify.platform.api.ISpectrumApi;
import net.minecraft.client.gui.components.toasts.Toast;

public class SpectrumFallback implements ISpectrumApi {
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
