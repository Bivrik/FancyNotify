package net.bivrik.fancynotify.mixin;

import net.bivrik.fancynotify.FancyNotify;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.notification.gui.SystemNotification;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SystemToast.class)
public class SystemToastMixin {
    @Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/client/gui/components/toasts/SystemToast$SystemToastId;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;)V")
    private void onInit(SystemToast.SystemToastId id, Component title, Component message, CallbackInfo info) {
        fancyNotify$add(id, title, message);
    }

    @Inject(at = @At("HEAD"), method = "add", cancellable = true)
    private static void onAdded(ToastManager toastManager, SystemToast.SystemToastId id, Component title, Component message, CallbackInfo info) {
        info.cancel();

        fancyNotify$add(id, title, message);
    }

    @Inject(at = @At("HEAD"), method = "addOrUpdate", cancellable = true)
    private static void onAddedOrUpdated(ToastManager toastManager, SystemToast.SystemToastId id, Component title, Component message, CallbackInfo info) {
        info.cancel();

        fancyNotify$add(id, title, message);
    }

    @Inject(at = @At("HEAD"), method = "forceHide(Lnet/minecraft/client/gui/components/toasts/ToastManager;Lnet/minecraft/client/gui/components/toasts/SystemToast$SystemToastId;)V", cancellable = true)
    private static void onForcedHide(ToastManager toastManager, SystemToast.SystemToastId id, CallbackInfo info) {
        info.cancel();

        NotificationManager manager = FancyNotify.getInstance().getNotificationManager();
        if (manager != null) {
            manager.remove(SystemNotification.class, SystemNotification.Ids.fromSystemToastId(id));
        }
    }

    @Unique
    private static void fancyNotify$add(SystemToast.SystemToastId id, Component title, Component message) {
        NotificationManager manager = FancyNotify.getInstance().getNotificationManager();
        if (manager != null) {
            manager.add(new SystemNotification(manager, SystemNotification.Ids.fromSystemToastId(id), title, message));
        }
    }
}
