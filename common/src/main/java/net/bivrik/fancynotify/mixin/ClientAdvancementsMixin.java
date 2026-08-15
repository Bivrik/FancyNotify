package net.bivrik.fancynotify.mixin;

import net.bivrik.fancynotify.FancyNotify;
import net.bivrik.fancynotify.accessor.IAdvancementHolderAccessor;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.notification.gui.AdvancementNotification;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.multiplayer.ClientAdvancements;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientAdvancements.class)
public class ClientAdvancementsMixin {
    @Redirect(method = "update", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/components/toasts/ToastComponent;addToast(Lnet/minecraft/client/gui/components/toasts/Toast;)V"
    ))
    private void onUpdate(ToastComponent instance, Toast toast) {
        NotificationManager manager = FancyNotify.getInstance().getNotificationManager();
        AdvancementToast advancementToast = (AdvancementToast) toast;
        DisplayInfo displayInfo = ((IAdvancementHolderAccessor) advancementToast).getAdvancement().getDisplay();
        if (displayInfo != null) {
            manager.add(new AdvancementNotification(manager, displayInfo.getTitle(), displayInfo.getFrame(), displayInfo.getIcon()));
        }
    }
}
