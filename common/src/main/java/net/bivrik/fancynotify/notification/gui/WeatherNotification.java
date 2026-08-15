package net.bivrik.fancynotify.notification.gui;

import net.bivrik.fancynotify.WeatherType;
import net.bivrik.fancynotify.notification.ExpandableNotification;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.utility.Identifiers;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.awt.*;

public class WeatherNotification extends ExpandableNotification {
    private static final Identifier BACKGROUND = Identifiers.of("notifications/weather");
    private static final int COLOR = new Color(174, 203, 255).getRGB();

    private Identifier icon;

    public WeatherNotification(NotificationManager manager, WeatherType weather) {
        super(manager, weather.getDisplayName(), Component.empty());

        this.icon = weather.getIcon();
    }

    @Override
    public boolean shouldDisplay() {
        return this.filtersConfig.isWeatherNotificationEnabled.get();
    }

    @Override
    protected void expand(ExpandableNotification expansion) {
        if (expansion instanceof WeatherNotification weatherNotification) {
            setDisplay(weatherNotification.getTitle(), Component.empty());
            this.icon = weatherNotification.icon;
        }
    }

    @Override
    public void draw(GuiGraphicsExtractor GuiGraphicsExtractor) {
        drawSprite(GuiGraphicsExtractor, BACKGROUND, 0, 0, getWidth(), getHeight());
        drawText(GuiGraphicsExtractor, getTitle(), getTextOffset(), 8, COLOR);
        drawSprite(GuiGraphicsExtractor, this.icon, 5, 1, 20, 20);
    }
}
