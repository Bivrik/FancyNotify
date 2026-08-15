package net.bivrik.fancynotify.mixin;

import net.bivrik.fancynotify.FancyNotify;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.notification.gui.SystemNotification;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SystemToast.class)
public class SystemToastMixin {
    @Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/client/gui/components/toasts/SystemToast$SystemToastIds;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;)V")
    private void onInit(SystemToast.SystemToastIds id, Component title, Component message, CallbackInfo info) {
        fancyNotify$add(id, title, message);
    }

    @Inject(at = @At("HEAD"), method = "add", cancellable = true)
    private static void onAdded(ToastComponent toastComponent, SystemToast.SystemToastIds id, Component title, Component message, CallbackInfo info) {
        info.cancel();

        fancyNotify$add(id, title, message);
    }

    @Inject(at = @At("HEAD"), method = "addOrUpdate", cancellable = true)
    private static void onAddedOrUpdated(ToastComponent toastComponent, SystemToast.SystemToastIds id, Component title, Component message, CallbackInfo info) {
        info.cancel();

        fancyNotify$add(id, title, message);
    }

    @Inject(at = @At("HEAD"), method = "multiline", cancellable = true)
    private static void onMultiline(Minecraft minecraft, SystemToast.SystemToastIds id, Component title, Component message, CallbackInfoReturnable<SystemToast> info) {
        info.cancel();

        fancyNotify$add(id, title, message);
    }

    @Unique
    private static void fancyNotify$add(SystemToast.SystemToastIds id, Component title, Component message) {
        NotificationManager manager = FancyNotify.getInstance().getNotificationManager();
        manager.add(new SystemNotification(manager, SystemNotification.Identifier.fromSystemToastId(id), title, message));
    }
}
