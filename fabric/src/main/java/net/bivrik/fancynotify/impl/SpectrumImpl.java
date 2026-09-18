package net.bivrik.fancynotify.impl;

import de.dafuqs.spectrum.progression.toast.MessageToast;
import de.dafuqs.spectrum.progression.toast.RevelationToast;
import de.dafuqs.spectrum.progression.toast.UnlockedRecipeToast;
import net.bivrik.compat.spectrum.MessageToastAccessor;
import net.bivrik.compat.spectrum.RevelationToastAccessor;
import net.bivrik.compat.spectrum.UnlockedRecipeToastAccessor;
import net.bivrik.compat.spectrum.notification.MessageNotification;
import net.bivrik.compat.spectrum.notification.RevelationNotification;
import net.bivrik.compat.spectrum.notification.UnlockedRecipeNotification;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.platform.services.ISpectrumApi;
import net.minecraft.client.gui.components.toasts.Toast;

public class SpectrumImpl implements ISpectrumApi {
    @Override
    public boolean tryHandleMessageToast(Toast toast, NotificationManager notificationManager) {
        if (!(toast instanceof MessageToast)) {
            return false;
        }

        MessageToastAccessor accessor = ((MessageToastAccessor) toast);
        notificationManager.add(new MessageNotification(notificationManager, accessor.getTitleText(), accessor.getMessageText(), accessor.getItemStack(), accessor.getSoundEvent()));
        return true;
    }

    @Override
    public boolean tryHandleRevelationToast(Toast toast, NotificationManager notificationManager) {
        if (!(toast instanceof RevelationToast)) {
            return false;
        }

        RevelationToastAccessor accessor = ((RevelationToastAccessor) toast);
        notificationManager.add(new RevelationNotification(notificationManager, accessor.getItemStack(), accessor.getSoundEvent()));
        return true;
    }

    @Override
    public boolean tryHandleUnlockedRecipeToast(Toast toast, NotificationManager notificationManager) {
        if (!(toast instanceof UnlockedRecipeToast)) {
            return false;
        }

        UnlockedRecipeToastAccessor accessor = ((UnlockedRecipeToastAccessor) toast);
        notificationManager.add(new UnlockedRecipeNotification(notificationManager, accessor.getTitle(), accessor.getText(), accessor.getItemStacks(), accessor.getSoundEvent()));
        return true;
    }
}
