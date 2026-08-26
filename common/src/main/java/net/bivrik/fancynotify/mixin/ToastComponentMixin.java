package net.bivrik.fancynotify.mixin;

import net.bivrik.fancynotify.FancyNotify;
import net.bivrik.fancynotify.accessor.IAdvancementHolderAccessor;
import net.bivrik.fancynotify.core.Log;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.notification.gui.AdvancementNotification;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(value = ToastComponent.class, priority = 1500)
public class ToastComponentMixin {
    // Entrypoint of most vanilla toasts, but since in vanilla there
    // are no expendable toasts, there are a lot of static addOrUpdate()
    // methods. Therefore, here we only catch simple toasts, like
    // advancements or from different mods. Every other toast
    // is handled by vanilla system, therefore better compatibility!
    @Inject(at = @At("HEAD"), method = "addToast", cancellable = true)
    private void onAddedToast(Toast toast, CallbackInfo info) {
        if (toast instanceof AdvancementToast advancementToast) {
            Optional<DisplayInfo> optionalDisplay = ((IAdvancementHolderAccessor) advancementToast).getAdvancementHolder().value().display();
            NotificationManager manager = FancyNotify.getInstance().getNotificationManager();
            optionalDisplay.ifPresent(displayInfo -> manager.add(new AdvancementNotification(manager, displayInfo.getTitle(), displayInfo.getType(), displayInfo.getIcon())));
        }

        if (toast != null) {
            Log.info("Registered unsupported toast. Using vanilla toast system for {}", toast.getClass().getSimpleName());
        } else {
            info.cancel();
            Log.warn("Ugh... null toast?");
        }
    }

    // Clears all the toasts and notifications when leaving world
    @Inject(at = @At("HEAD"), method = "clear")
    private void onCleared(CallbackInfo info) {
        FancyNotify.getInstance().getNotificationManager().clear();
    }

    @Inject(at = @At("HEAD"), method = "render")
    private void onRendered(GuiGraphics guiGraphics, CallbackInfo info) {
        NotificationManager manager = FancyNotify.getInstance().getNotificationManager();
        manager.update();
        manager.render(guiGraphics);
    }
}
