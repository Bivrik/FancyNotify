package net.bivrik.fancynotify.platform.impl;

import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.platform.services.IFieldGuideApi;
import net.minecraft.client.gui.components.toasts.Toast;

public class FieldGuideImpl implements IFieldGuideApi {
    @Override
    public boolean tryHandleToast(Toast toast, NotificationManager notificationManager) {
        return false;
    }
}
