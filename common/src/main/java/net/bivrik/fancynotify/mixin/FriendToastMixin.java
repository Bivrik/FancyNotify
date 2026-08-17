package net.bivrik.fancynotify.mixin;

import net.bivrik.fancynotify.FancyNotify;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.notification.gui.FriendNotification;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.toasts.FriendToast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.component.ResolvableProfile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FriendToast.class)
public class FriendToastMixin {
    @Inject(at = @At("HEAD"), method = "add(Lnet/minecraft/client/gui/components/toasts/ToastManager;Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/component/ResolvableProfile;Lnet/minecraft/network/chat/Component;)V", cancellable = true)
    private static void onAdded(ToastManager toastManager, Font font, ResolvableProfile skinProfile, Component message, CallbackInfo info) {
        info.cancel();

        NotificationManager notificationManager = FancyNotify.getInstance().getNotificationManager();
        notificationManager.add(new FriendNotification(notificationManager, message, skinProfile));
    }
}
