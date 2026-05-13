package net.bivrik.fancynotify.mixin;

import net.bivrik.fancynotify.ITutorialToastAccessor;
import net.bivrik.fancynotify.core.Common;
import net.bivrik.fancynotify.gui.Notification;
import net.bivrik.fancynotify.gui.TutorialNotification;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.client.tutorial.Tutorial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(Tutorial.class)
public class TutorialMixin {
    @Unique
    private final List<Notification> fancyNotify$notifications = new ArrayList<>();

    @Inject(at = @At("HEAD"), method = "addTimedToast", cancellable = true)
    public void onAddedTimedToast(TutorialToast tutorialToast, int durationTicks, CallbackInfo info) {
        /*info.cancel();

        var manager = Common.getNotificationManager();
        var hui = ((ITutorialToastAccessor) tutorialToast);
        var n = new TutorialNotification(manager, hui.getIcon(), hui.getTitle(), hui.getDescription());
        fancyNotify$notifications.add(n);
        manager.add(n);*/
    }

    @Inject(at = @At("HEAD"), method = "removeTimedToast", cancellable = true)
    public void onRemovedTimedToast(TutorialToast tutorialToast, CallbackInfo info) {
        /*info.cancel();

        //Common.getTutorialManager().removeTimed();
        fancyNotify$notifications.remove(tutorialToast);*/
    }

    @Inject(at = @At("HEAD"), method = "tick", cancellable = true)
    public void onTick(CallbackInfo info) {
        //info.cancel();
    }
}
