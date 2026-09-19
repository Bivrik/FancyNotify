package net.bivrik.fancynotify.platform.services;

import net.bivrik.fancynotify.notification.NotificationManager;
import net.minecraft.client.gui.components.toasts.Toast;

public interface IFieldGuideApi {

    boolean tryHandleToast(Toast toast, NotificationManager notificationManager);
}
