package net.bivrik.fancynotify.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.NativeImage;
import net.bivrik.fancynotify.FancyNotify;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.notification.gui.ScreenshotNotification;
import net.minecraft.client.Screenshot;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.function.Consumer;

@Mixin(Screenshot.class)
public class ScreenshotMixin {
    @Inject(method = "_grab", at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/client/Screenshot;takeScreenshot(Lcom/mojang/blaze3d/pipeline/RenderTarget;)Lcom/mojang/blaze3d/platform/NativeImage;"))
    private static void onScreenshotTaken(File gameDirectory, String screenshotName, RenderTarget buffer, Consumer<Component> messageConsumer, CallbackInfo info, @Local NativeImage nativeImage) {
        NotificationManager manager = FancyNotify.getInstance().getNotificationManager();
        NativeImage imagePreview = new NativeImage(nativeImage.format(), nativeImage.getWidth(), nativeImage.getHeight(), false);
        imagePreview.copyFrom(nativeImage);
        manager.add(new ScreenshotNotification(manager, imagePreview));
    }
}
