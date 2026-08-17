package net.bivrik.fancynotify.mixin;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.textures.GpuTexture;
import net.bivrik.fancynotify.FancyNotify;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.notification.gui.ScreenshotNotification;
import net.minecraft.client.Screenshot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Consumer;

@Mixin(Screenshot.class)
public class ScreenshotMixin {
    // A bit fragile, I agree, but I didn't come up with anything better
    @Inject(method = "lambda$takeScreenshot$1",
            at = @At(
                value = "INVOKE",
                target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private static void onScreenshotTaken(GpuBuffer buffer, int height, int downscaleFactor, int width, GpuTexture sourceTexture, Consumer<NativeImage> callback, CallbackInfo info,
                          NativeImage image) {
        NativeImage preview = new NativeImage(image.format(), image.getWidth(), image.getHeight(), false);
        preview.copyFrom(image);
        NotificationManager manager = FancyNotify.getInstance().getNotificationManager();
        manager.add(new ScreenshotNotification(manager, preview));
    }
}
