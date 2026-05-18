package net.bivrik.fancynotify.gui;

import net.bivrik.fancynotify.NotificationManager;
import net.bivrik.fancynotify.ResourceLocations;
import net.bivrik.fancynotify.WeatherType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class WeatherNotification extends ExpandableNotification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/weather");
    private static final int COLOR = new Color(174, 203, 255).getRGB();
    private WeatherType weather;
    private ResourceLocation icon;

    public WeatherNotification(NotificationManager manager, WeatherType weather) {
        super(manager);

        setValues(weather);
    }

    private void setValues(WeatherType weather) {
        this.weather = weather;

        this.setDisplay(weather.getDisplayName(), null);
        this.icon = weather.getIcon();
    }

    @Override
    public boolean shouldDisplay() {
        return this.filtersConfig.isWeatherNotificationEnabled.get();
    }

    @Override
    public int getHeight() {
        return 23;
    }

    @Override
    protected void expand(ExpandableNotification expansion) {
        if (expansion instanceof WeatherNotification weatherNotification) {
            setValues(weatherNotification.weather);
        }
    }

    @Override
    public void draw(GuiGraphics guiGraphics) {
        drawSprite(guiGraphics, BACKGROUND, 0, 0, this.getWidth(), this.getHeight());
        drawText(guiGraphics, title, 29, 8, COLOR);
        drawSprite(guiGraphics, icon, 5, 1, 20, 20);
    }
}
