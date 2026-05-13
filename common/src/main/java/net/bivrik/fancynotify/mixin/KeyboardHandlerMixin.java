package net.bivrik.fancynotify.mixin;

import net.bivrik.fancynotify.core.Common;
import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {
    @Inject(at = @At("HEAD"), method = "keyPress")
    public void onKeyPressed(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo info) {
        Common.onKeyPressed(key);
    }
}
