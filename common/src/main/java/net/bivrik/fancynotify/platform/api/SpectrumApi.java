package net.bivrik.fancynotify.platform.api;

import net.bivrik.fancynotify.api.NotificationManager;
import net.minecraft.client.gui.components.toasts.Toast;

public interface SpectrumApi {

    boolean tryHandleMessageToast(Toast toast, NotificationManager notificationManager);

    boolean tryHandleRevelationToast(Toast toast, NotificationManager notificationManager);

    boolean tryHandleUnlockedRecipeToast(Toast toast, NotificationManager notificationManager);
}
