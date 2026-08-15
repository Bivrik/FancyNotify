package net.bivrik.fancynotify.mixin;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.bivrik.fancynotify.FancyNotify;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;flush()V"
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void onRendered(float partialTick, long nanoTime, boolean renderLevel, CallbackInfo info,
                            int mouseX, int MouseY, Window window,
                            Matrix4f matrix, PoseStack stack, GuiGraphics guiGraphics) {
        FancyNotify.getInstance().onGuiRender(guiGraphics, partialTick);
    }
}
