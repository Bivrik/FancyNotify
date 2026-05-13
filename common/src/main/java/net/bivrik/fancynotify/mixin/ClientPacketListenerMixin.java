package net.bivrik.fancynotify.mixin;

import net.bivrik.fancynotify.NotificationManager;
import net.bivrik.fancynotify.WeatherType;
import net.bivrik.fancynotify.core.Common;
import net.bivrik.fancynotify.gui.WeatherNotification;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
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
            NotificationManager manager = Common.getNotificationManager();
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
}
