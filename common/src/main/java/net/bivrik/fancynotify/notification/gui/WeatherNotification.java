package net.bivrik.fancynotify.notification.gui;

import net.bivrik.fancynotify.WeatherType;
import net.bivrik.fancynotify.notification.ExpandableNotification;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class WeatherNotification extends ExpandableNotification {
    private static final int COLOR = new Color(174, 203, 255).getRGB();

    private ResourceLocation icon;

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
    public void draw(GuiGraphics guiGraphics) {
        drawBackground(guiGraphics, 0, 160);
        drawText(guiGraphics, getTitle(), getTextOffset(), 8, COLOR);
        drawTexture(guiGraphics, icon, 5, 1, 20, 20, 20, 20);
    }
}
