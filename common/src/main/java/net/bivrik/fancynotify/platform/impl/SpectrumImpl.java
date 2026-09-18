package net.bivrik.fancynotify.platform.impl;

import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.platform.services.ISpectrumApi;
import net.minecraft.client.gui.components.toasts.Toast;

public class SpectrumImpl implements ISpectrumApi {
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
