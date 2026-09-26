package net.bivrik.fancynotify.platform.api;

import net.bivrik.fancynotify.api.NotificationManager;
import net.minecraft.client.gui.components.toasts.Toast;

public interface FieldGuideApi {

    boolean tryHandleToast(Toast toast, NotificationManager notificationManager);
}
