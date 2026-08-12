package net.bivrik.fancynotify.mixin;

import com.mojang.authlib.GameProfile;
import net.bivrik.fancynotify.NotificationManager;
import net.bivrik.fancynotify.WeatherType;
import net.bivrik.fancynotify.core.FancyNotify;
import net.bivrik.fancynotify.core.Log;
import net.bivrik.fancynotify.gui.PlayerLoginNotification;
import net.bivrik.fancynotify.gui.WeatherNotification;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.social.PlayerSocialManager;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
    @Shadow
    private ClientLevel level;

    @Shadow
    @Nullable
    public abstract PlayerInfo getPlayerInfo(UUID uuid);

    @Unique
    private boolean fancyNotify$isRaining;
    @Unique
    private boolean fancyNotify$isThundering;
    @Unique
    private WeatherType fancyNotify$currentWeatherType = WeatherType.CLEAR;

    @Inject(at = @At("RETURN"), method = "handleGameEvent")
    private void onHandledGameEvent(ClientboundGameEventPacket packet, CallbackInfo info) {
        float value = packet.getParam();
        ClientboundGameEventPacket.Type event = packet.getEvent();
        boolean isWeatherEvent = false;
        if (event.equals(ClientboundGameEventPacket.RAIN_LEVEL_CHANGE)) {
            fancyNotify$isRaining = value >= 0.5f;
            isWeatherEvent = true;
        } else if (event.equals(ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE)) {
            fancyNotify$isThundering = value >= 0.5f;
            isWeatherEvent = true;
        }

        if (!isWeatherEvent) {
            return;
        }

        WeatherType temp = fancyNotify$getWeatherType();
        if (temp != fancyNotify$currentWeatherType) {
            fancyNotify$currentWeatherType = temp;
            NotificationManager manager = FancyNotify.getInstance().getNotificationManager();
            manager.add(new WeatherNotification(manager, fancyNotify$currentWeatherType));
        }
    }

    @Unique
    private WeatherType fancyNotify$getWeatherType() {
        if (!fancyNotify$isRaining) {
            return WeatherType.CLEAR;
        }

        if (!fancyNotify$isThundering) {
            return WeatherType.RAIN;
        }

        return WeatherType.THUNDER;
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

        GameProfile profile = playerInfo.getProfile();
        Minecraft.getInstance().getSkinManager().getOrLoad(profile).thenAcceptAsync(skin -> {
            Player player = this.level.getPlayerByUUID(profile.getId());
            boolean hasHat = player != null && player.isModelPartShown(PlayerModelPart.HAT);
            NotificationManager manager = FancyNotify.getInstance().getNotificationManager();
            manager.add(new PlayerLoginNotification(manager, profile.getName(), skin.texture(), hasHat));

            Log.info("====================");
            Log.info("Profile: " + profile);
            Log.info("UUID from profile: " + profile.getId());
            Log.info("Name from profile: " + profile.getName());
            Log.info("====================");
        });
    }
}
