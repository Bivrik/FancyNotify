package net.bivrik.fancynotify.platform.fallback;

import net.bivrik.fancynotify.api.NotificationManager;
import net.bivrik.fancynotify.platform.api.FieldGuideApi;
import net.minecraft.client.gui.components.toasts.Toast;

public class FieldGuideFallback implements FieldGuideApi {
    @Override
    public boolean tryHandleToast(Toast toast, NotificationManager notificationManager) {
        return false;
    }
}
