package net.bivrik.fancynotify.gui.notification;

import net.bivrik.fancynotify.api.Notification;
import net.bivrik.fancynotify.api.NotificationGraphics;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.bivrik.fancynotify.weather.WeatherType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class WeatherNotification extends FancyExpandableNotification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/weather");
    private static final Component MESSAGE = Component.empty();
    private static final int TITLE_COLOR = new Color(174, 203, 255).getRGB();

    private ResourceLocation icon;

    public WeatherNotification(WeatherType weather) {
        super(weather.getDisplayName(), MESSAGE);

        this.icon = weather.getIcon();
    }

    @Override
    public boolean shouldDisplay() {
        return this.filters.isWeatherNotificationEnabled.get();
    }

    @Override
    public void expand(Notification expansion) {
        WeatherNotification other = (WeatherNotification) expansion;

        setDisplay(other.getTitle(), MESSAGE);
        icon = other.icon;
    }

    @Override
    public void render(NotificationGraphics graphics, float partialTick) {
        graphics.sprite(BACKGROUND, 0, 0, getWidth(), getHeight());
        graphics.text(getTitle(), getTextOffset(), 8, TITLE_COLOR);
        graphics.sprite(icon, 5, 1, 20, 20);
    }
}
