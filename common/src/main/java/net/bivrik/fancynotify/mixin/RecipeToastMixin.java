package net.bivrik.fancynotify.mixin;

import net.bivrik.fancynotify.NotificationManager;
import net.bivrik.fancynotify.core.Common;
import net.bivrik.fancynotify.gui.RecipeNotification;
import net.minecraft.client.gui.components.toasts.RecipeToast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeToast.class)
public class RecipeToastMixin {
    @Inject(at = @At("HEAD"), method = "addOrUpdate", cancellable = true)
    private static void onAddedOrUpdated(ToastComponent toastComponent, RecipeHolder<?> recipe, CallbackInfo info) {
        info.cancel();

        NotificationManager manager = Common.getNotificationManager();
        manager.add(new RecipeNotification(manager, recipe));
    }
}
