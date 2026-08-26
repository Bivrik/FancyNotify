package net.bivrik.fancynotify.mixin;

import com.mojang.blaze3d.platform.NativeImage;
import net.bivrik.fancynotify.FancyNotify;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.notification.gui.ScreenshotNotification;
import net.minecraft.client.Screenshot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.io.File;
import java.util.function.Consumer;

@Mixin(Screenshot.class)
public class ScreenshotMixin {
    // A bit fragile, I agree, but I didn't come up with anything better
    @Inject(method="lambda$grab$2",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/TracingExecutor;execute(Ljava/lang/Runnable;)V"
    ))
    private static void onScreenshotGrabbed(File workDir, String forceName, Consumer<Component> callback, NativeImage image, CallbackInfo info) {
        NotificationManager manager = FancyNotify.getInstance().getNotificationManager();
        if (manager != null) {
            NativeImage preview = new NativeImage(image.format(), image.getWidth(), image.getHeight(), false);
            preview.copyFrom(image);
            manager.add(new ScreenshotNotification(manager, preview));
        }
    }
}
