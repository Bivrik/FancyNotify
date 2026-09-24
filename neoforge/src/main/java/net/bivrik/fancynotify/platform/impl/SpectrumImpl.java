package net.bivrik.fancynotify.platform.impl;

import de.dafuqs.spectrum.progression.toast.MessageToast;
import de.dafuqs.spectrum.progression.toast.RevelationToast;
import de.dafuqs.spectrum.progression.toast.UnlockedRecipeToast;
import net.bivrik.fancynotify.api.NotificationManager;
import net.bivrik.fancynotify.compat.spectrum.MessageToastAccessor;
import net.bivrik.fancynotify.compat.spectrum.RevelationToastAccessor;
import net.bivrik.fancynotify.compat.spectrum.UnlockedRecipeToastAccessor;
import net.bivrik.fancynotify.compat.spectrum.notification.MessageNotification;
import net.bivrik.fancynotify.compat.spectrum.notification.RevelationNotification;
import net.bivrik.fancynotify.compat.spectrum.notification.UnlockedRecipeNotification;
import net.bivrik.fancynotify.platform.api.ISpectrumApi;
import net.minecraft.client.gui.components.toasts.Toast;

public class SpectrumImpl implements ISpectrumApi {
    @Override
    public boolean tryHandleMessageToast(Toast toast, NotificationManager notificationManager) {
        if (!(toast instanceof MessageToast)) {
            return false;
        }

        MessageToastAccessor accessor = ((MessageToastAccessor) toast);
        notificationManager.add(new MessageNotification(accessor.getTitleText(), accessor.getMessageText(), accessor.getItemStack(), accessor.getSoundEvent()));
        return true;
    }

    @Override
    public boolean tryHandleRevelationToast(Toast toast, NotificationManager notificationManager) {
        if (!(toast instanceof RevelationToast)) {
            return false;
        }

        RevelationToastAccessor accessor = ((RevelationToastAccessor) toast);
        notificationManager.add(new RevelationNotification(accessor.getItemStack(), accessor.getSoundEvent()));
        return true;
    }

    @Override
    public boolean tryHandleUnlockedRecipeToast(Toast toast, NotificationManager notificationManager) {
        if (!(toast instanceof UnlockedRecipeToast)) {
            return false;
        }

        UnlockedRecipeToastAccessor accessor = ((UnlockedRecipeToastAccessor) toast);
        notificationManager.add(new UnlockedRecipeNotification(accessor.getTitle(), accessor.getText(), accessor.getItemStacks(), accessor.getSoundEvent()));
        return true;
    }
}
