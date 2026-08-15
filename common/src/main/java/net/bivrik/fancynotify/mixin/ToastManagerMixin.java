package net.bivrik.fancynotify.mixin;

import net.bivrik.fancynotify.FancyNotify;
import net.bivrik.fancynotify.core.Log;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ToastManager.class)
public class ToastManagerMixin {
    // Mixin into the entry point of vanilla toasts, because there are a lot of static
    // classes. All the modded ones will go through here, and if we don't catch it
    // here it will just render as usual toast. Better compatibility!
    // In the future just add instanceof to catch them or mixin as well, depends on
    // the situation.
    @Inject(at = @At("HEAD"), method = "addToast", cancellable = true)
    private void onAddedToast(Toast toast, CallbackInfo info) {
        if (toast != null) {
            Log.info("Registered unsupported toast. Using vanilla toast system for {}", toast.getClass().getSimpleName());
        } else {
            info.cancel();
            Log.error("Ugh... null toast?");
        }
    }

    @Inject(at = @At("HEAD"), method = "clear")
    private void onCleared(CallbackInfo info) {
        FancyNotify.getInstance().getNotificationManager().clear();
    }

    @Inject(at = @At("HEAD"), method = "extractRenderState")
    private void onRendered(GuiGraphicsExtractor graphics, CallbackInfo info) {
        NotificationManager manager = FancyNotify.getInstance().getNotificationManager();
        manager.update();
        manager.render(graphics);
    }
}
