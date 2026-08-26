package net.bivrik.fancynotify.mixin;

import com.mojang.authlib.GameProfile;
import net.bivrik.fancynotify.FancyNotify;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.notification.gui.PlayerLoginNotification;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.social.PlayerSocialManager;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
    @Shadow
    private ClientLevel level;

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
    private void onAddedPlayer(PlayerSocialManager playerSocialManager, PlayerInfo playerInfo) {
        playerSocialManager.addPlayer(playerInfo);

        NotificationManager manager = FancyNotify.getInstance().getNotificationManager();
        if (manager == null) {
            return;
        }

        if (FancyNotify.getInstance().getConfigManager().getFiltersConfig().isLoginPlayerNotificationEnabled.get()) {
            GameProfile profile = playerInfo.getProfile();
            Minecraft.getInstance().getSkinManager().getOrLoad(profile).thenAcceptAsync(skin -> {
                Player player = this.level.getPlayerByUUID(profile.getId());
                boolean hasHat = player != null && player.isModelPartShown(PlayerModelPart.HAT);
                manager.add(new PlayerLoginNotification(manager, profile.getName(), skin.texture(), hasHat));
            });
        }
    }
}
