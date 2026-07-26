package net.bivrik.fancynotify.mixin;

import net.bivrik.fancynotify.IAdvancementHolderAccessor;
import net.bivrik.fancynotify.NotificationManager;
import net.bivrik.fancynotify.core.Common;
import net.bivrik.fancynotify.core.Log;
import net.bivrik.fancynotify.gui.AdvancementNotification;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ToastComponent.class)
public class ToastComponentMixin {
    // Some toasts are just calling method addToast from ToastComponent so we capture them here.
    // Otherwise, mixin into the entrypoint (usually static classes from damn toasts themselves).
    @Inject(at = @At("HEAD"), method = "addToast", cancellable = true)
    private void onAddedToast(Toast toast, CallbackInfo info) {
        info.cancel();

        NotificationManager manager = Common.getNotificationManager();
        // Advancement Notifications
        if (toast instanceof AdvancementToast advancementToast) {
            Optional<DisplayInfo> optionalDisplay = ((IAdvancementHolderAccessor) advancementToast).getAdvancementHolder().value().display();
            optionalDisplay.ifPresent(displayInfo -> manager.add(new AdvancementNotification(manager, displayInfo.getTitle(), displayInfo.getType(), displayInfo.getIcon())));
        }
        // System Notifications
        else if (toast instanceof SystemToast) {
            // Just ignoring system toast because it should've been transformed into system notification before
            Log.info("Just ignoring system toast because it should've been transformed into system notification before");
        }
        // Catch all unique toasts here and warn
        else if (toast != null) {
            Log.warn("Failed to create a notification from toast: {} ({})", toast.getClass(), toast);
        } else {
            Log.warn("Null toast???");
        }
    }

    @Inject(at = @At("HEAD"), method = "clear", cancellable = true)
    private void onCleared(CallbackInfo info) {
        info.cancel();

        Common.getNotificationManager().clear();
    }

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void onRendered(GuiGraphics guiGraphics, CallbackInfo info) {
        info.cancel();

        NotificationManager manager = Common.getNotificationManager();
        manager.update();
        manager.render(guiGraphics);
    }
}
