package net.bivrik.fancynotify.mixin;

import net.bivrik.fancynotify.FancyNotify;
import net.bivrik.fancynotify.accessor.IAdvancementHolderAccessor;
import net.bivrik.fancynotify.core.Log;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.notification.gui.AdvancementNotification;
import net.bivrik.fancynotify.platform.Services;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
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
            Optional<DisplayInfo> optionalDisplay = ((IAdvancementHolderAccessor) toast).getAdvancementHolder().value().display();
            optionalDisplay.ifPresent(display -> manager.add(new AdvancementNotification(manager, display.getTitle(), display.getType(), display.getIcon())));
            info.cancel();
            return;
        }

        if (Services.PLATFORM.isModLoaded("spectrum")) {
            if (Services.SPECTRUM_API.tryHandleMessageToast(toast, manager)) {
                info.cancel();
                return;
            }

            if (Services.SPECTRUM_API.tryHandleRevelationToast(toast, manager)) {
                info.cancel();
                return;
            }

            if (Services.SPECTRUM_API.tryHandleUnlockedRecipeToast(toast, manager)) {
                info.cancel();
                return;
            }
        }

        if (Services.PLATFORM.isModLoaded("puffish_skills")) {
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
        NotificationManager manager = FancyNotify.getInstance().getNotificationManager();
        if (manager != null) {
            manager.clear();
        }
    }

    @Inject(at = @At("HEAD"), method = "render")
    private void onRendered(GuiGraphics guiGraphics, CallbackInfo info) {
        NotificationManager manager = FancyNotify.getInstance().getNotificationManager();
        if (manager != null) {
            manager.update();
            manager.render(guiGraphics);
        }
    }
}
