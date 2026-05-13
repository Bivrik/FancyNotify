package net.bivrik.fancynotify.mixin;

import net.bivrik.fancynotify.core.Common;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow
    static Minecraft instance;

    @Inject(at = @At("TAIL"), method = "<init>")
    private void onInit(CallbackInfo info) {
        Common.onMinecraftInit(instance);
    }

    @Inject(at = @At("RETURN"), method = "tick")
    private void onTick(CallbackInfo info) {
        Common.onClientTick();
    }
}
