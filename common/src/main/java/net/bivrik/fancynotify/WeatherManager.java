package net.bivrik.fancynotify;

import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.notification.gui.WeatherNotification;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;

public class WeatherManager {
    private final NotificationManager notificationManager;

    private WeatherType currentWeather = WeatherType.CLEAR;
    private boolean isRaining;
    private boolean isThundering;

    public WeatherManager(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    public void onClientBoundGameEvent(ClientboundGameEventPacket packet) {
        ClientboundGameEventPacket.Type event = packet.getEvent();
        boolean isWeatherEvent = false;
        if (event.equals(ClientboundGameEventPacket.RAIN_LEVEL_CHANGE)) {
            isRaining = packet.getParam() >= 0.5f;
        } else if (event.equals(ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE)) {
            isThundering = packet.getParam() >= 0.5f;
        }

        if (!isWeatherEvent) {
            return;
        }

        WeatherType weather = getWeatherType();
        if (weather != currentWeather) {
            currentWeather = weather;
            notificationManager.add(new WeatherNotification(notificationManager, currentWeather));
        }
    }

    private WeatherType getWeatherType() {
        if (!isRaining) {
            return WeatherType.CLEAR;
        }

        if (!isThundering) {
            return WeatherType.RAIN;
        }

        return WeatherType.THUNDER;
    }
}
