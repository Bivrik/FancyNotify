package net.bivrik.fancynotify.mixin;

import net.bivrik.fancynotify.FancyNotify;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.notification.gui.PlayerLoginNotification;
import net.bivrik.fancynotify.notification.gui.SystemNotification;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.gui.screens.social.PlayerSocialManager;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.world.item.component.ResolvableProfile;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
    @Shadow
    @Final
    private static Component UNSECURE_SERVER_TOAST_TITLE;

    @Shadow
    @Final
    private static Component UNSERURE_SERVER_TOAST;

    @Inject(at = @At("RETURN"), method = "handleGameEvent")
    private void onHandledGameEvent(ClientboundGameEventPacket packet, CallbackInfo info) {
        FancyNotify.getInstance().getWeatherManager().onClientBoundGameEvent(packet);
    }

    // Why so many bugs and quirks???
    // What am I not understanding bruh
    @Redirect(method = "handlePlayerInfoUpdate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/social/PlayerSocialManager;addPlayer(Lnet/minecraft/client/multiplayer/PlayerInfo;)V"
            )
    )
    private void onAddedPlayer(PlayerSocialManager playerSocialManager, PlayerInfo info) {
        playerSocialManager.addPlayer(info);

        NotificationManager manager = FancyNotify.getInstance().getNotificationManager();
        if (manager != null) {
            if (FancyNotify.getInstance().getConfigManager().getFiltersConfig().isLoginPlayerNotificationEnabled.get()) {
                ResolvableProfile profile = ResolvableProfile.createUnresolved(info.getProfile().id());
                manager.add(new PlayerLoginNotification(manager, info.getProfile().name(), profile, info.showHat()));
            }
        }
    }

    @Redirect(method = "handleLogin",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/toasts/ToastManager;addToast(Lnet/minecraft/client/gui/components/toasts/Toast;)V"
            )
    )
    private void onHandledLogin(ToastManager instance, Toast toast) {
        NotificationManager manager = FancyNotify.getInstance().getNotificationManager();
        if (manager != null) {
            manager.add(new SystemNotification(manager, SystemNotification.Ids.UNSECURE_SERVER_WARNING, UNSECURE_SERVER_TOAST_TITLE, UNSERURE_SERVER_TOAST));
        }
    }
}
