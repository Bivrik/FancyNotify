package net.bivrik.fancynotify.mixin;

import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.gui.screens.worldselection.EditWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EditWorldScreen.class)
public class EditWorldScreenMixin {
    @Redirect(method = "lambda$makeBackupAndShowToast$1", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/components/toasts/ToastManager;addToast(Lnet/minecraft/client/gui/components/toasts/Toast;)V"
    ))
    private static void onBackupMadeAndToastShown1(ToastManager toastManager, Toast toast) {
        // Disable toast from creating
    }

    @Redirect(method = "lambda$makeBackupAndShowToast$2", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/components/toasts/ToastManager;addToast(Lnet/minecraft/client/gui/components/toasts/Toast;)V"
    ))
    private static void onBackupMadeAndToastShown2(ToastManager toastManager, Toast toast) {
        // Disable toast from creating
    }
}
