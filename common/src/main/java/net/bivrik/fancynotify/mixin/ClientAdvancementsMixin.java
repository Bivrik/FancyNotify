package net.bivrik.fancynotify.mixin;

import net.bivrik.fancynotify.FancyNotify;
import net.bivrik.fancynotify.accessor.IAdvancementHolderAccessor;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.notification.gui.AdvancementNotification;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.multiplayer.ClientAdvancements;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(ClientAdvancements.class)
public class ClientAdvancementsMixin {
    @Redirect(method = "update",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/toasts/ToastManager;addToast(Lnet/minecraft/client/gui/components/toasts/Toast;)V"
            )
    )
    private void onUpdated(ToastManager instance, Toast toast) {
        NotificationManager manager = FancyNotify.getInstance().getNotificationManager();
        AdvancementToast advancementToast = (AdvancementToast) toast;
        Optional<DisplayInfo> optionalDisplay = ((IAdvancementHolderAccessor) advancementToast).getAdvancementHolder().value().display();
        optionalDisplay.ifPresent(displayInfo -> manager.add(new AdvancementNotification(manager, displayInfo.getTitle(), displayInfo.getType(), displayInfo.getIcon().create())));
    }
}
