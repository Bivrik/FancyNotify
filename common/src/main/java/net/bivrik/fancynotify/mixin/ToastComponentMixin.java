package net.bivrik.fancynotify.mixin;

import net.bivrik.fancynotify.FancyNotify;
import net.bivrik.fancynotify.accessor.AdvancementHolderAccessor;
import net.bivrik.fancynotify.api.NotificationManager;
import net.bivrik.fancynotify.core.Constants;
import net.bivrik.fancynotify.core.Log;
import net.bivrik.fancynotify.gui.notification.AdvancementNotification;
import net.bivrik.fancynotify.notification.NotificationEngine;
import net.bivrik.fancynotify.platform.Services;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(value = ToastComponent.class, priority = 9000)
public class ToastComponentMixin {
    @Unique
    private static final String SIMPLE_TOAST = "net.puffish.skillsmod.client.gui.SimpleToast";

    // Entrypoint of most vanilla toasts, but since in vanilla there
    // are no expendable toasts, there are a lot of static addOrUpdate()
    // methods. Therefore, here we only catch simple toasts, like
    // advancements or from different mods. Every other toast
    // is handled by vanilla system, therefore better compatibility!
    @Inject(at = @At("HEAD"), method = "addToast", cancellable = true)
    private void onAddedToast(Toast toast, CallbackInfo info) {
        if (toast == null) { // already handled
            info.cancel();
            return;
        }

        NotificationManager manager = FancyNotify.getInstance().getNotificationManager();
        if (manager == null) {
            Log.info("Notification manager is null, handling {} by vanilla toast system", toast.getClass().getSimpleName());
            return;
        }

        if (toast instanceof AdvancementToast) {
            Optional<DisplayInfo> optionalDisplay = ((AdvancementHolderAccessor) toast).getAdvancementHolder().value().display();
            optionalDisplay.ifPresent(display -> manager.add(new AdvancementNotification(display.getTitle(), display.getType(), display.getIcon())));
            info.cancel();
            return;
        }

        if (Services.PLATFORM.isModLoaded(Constants.SPECTRUM_ID)) {
            if (Services.SPECTRUM.tryHandleMessageToast(toast, manager)) {
                info.cancel();
                return;
            }

            if (Services.SPECTRUM.tryHandleRevelationToast(toast, manager)) {
                info.cancel();
                return;
            }

            if (Services.SPECTRUM.tryHandleUnlockedRecipeToast(toast, manager)) {
                info.cancel();
                return;
            }
        }

        if (Services.PLATFORM.isModLoaded(Constants.FIELD_GUIDE_ID)) {
            if (Services.FIELD_GUIDE.tryHandleToast(toast, manager)) {
                info.cancel();
                return;
            }
        }

        if (Services.PLATFORM.isModLoaded(Constants.PUFFERFISHS_SKILLS)) {
            if (toast.getClass().getName().equals(SIMPLE_TOAST)) {
                info.cancel();
                return;
            }
        }

        Log.info("Registered unsupported toast. Using vanilla toast system for {}", toast.getClass().getSimpleName());
    }

    // Clears all the toasts and notifications when leaving world
    @Inject(at = @At("HEAD"), method = "clear")
    private void onCleared(CallbackInfo info) {
        NotificationEngine engine = FancyNotify.getInstance().getNotificationEngine();
        if (engine != null) {
            engine.clear();
        }
    }

    @Inject(at = @At("HEAD"), method = "render")
    private void onRendered(GuiGraphics guiGraphics, CallbackInfo info) {
        NotificationEngine engine = FancyNotify.getInstance().getNotificationEngine();
        if (engine != null) {
            engine.update();
            engine.render(guiGraphics);
        }
    }
}
