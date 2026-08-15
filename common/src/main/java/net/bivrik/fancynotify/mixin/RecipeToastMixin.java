package net.bivrik.fancynotify.mixin;

import net.bivrik.fancynotify.FancyNotify;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.notification.gui.RecipeNotification;
import net.minecraft.client.gui.components.toasts.RecipeToast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeToast.class)
public class RecipeToastMixin {
    @Inject(at = @At("HEAD"), method = "addOrUpdate", cancellable = true)
    private static void onAddedOrUpdated(ToastComponent toastComponent, Recipe<?> recipe, CallbackInfo info) {
        info.cancel();

        NotificationManager manager = FancyNotify.getInstance().getNotificationManager();
        manager.add(new RecipeNotification(manager, recipe));
    }
}
