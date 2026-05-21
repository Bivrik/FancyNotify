package net.bivrik.fancynotify.screen;

import net.bivrik.fancynotify.IntegerEditBox;
import net.bivrik.fancynotify.Slider;
import net.bivrik.fancynotify.config.ConfigManager;
import net.bivrik.fancynotify.config.GeneralConfig;
import net.bivrik.fancynotify.config.Setting;
import net.bivrik.fancynotify.core.Common;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class SettingsScreen extends UniversalScreen {
    private static final Component TITLE = Component.literal("Settings");
    private static final Component TRANSPARENCY_TITLE = Component.literal("Notifications Transparency");
    private static final Component WIDTH_TITLE = Component.literal("Notifications Width");
    private static final Component DISPLAY_TIME_TITLE = Component.literal("Notifications Time");
    private static final Component DISPLAY_TIME_TOOLTIP = Component.translatable("options.notifications.display_time.tooltip");;

    private final ConfigManager configManager;

    private Button backButton;
    private Slider transparencySlider;
    private IntegerEditBox widthEditBox;
    private Slider displayTimeSlider;

    protected SettingsScreen(Screen parent) {
        super(TITLE, parent);

        this.configManager = Common.getConfigManager();
    }

    @Override
    protected void init() {
        backButton = Button.builder(CommonComponents.GUI_BACK, button -> this.onClose())
                .bounds(this.width / 2 - Button.BIG_WIDTH / 2, this.height - Button.DEFAULT_HEIGHT - 6, Button.BIG_WIDTH, Button.DEFAULT_HEIGHT).build();
        this.addSimpleWidget(backButton);

        SettingsList list = new SettingsList(this.minecraft, this.width, this.height - 64 - 2, 32, 25, this);
        this.addSimpleWidget(list);

        Setting<Float> notificationTransparency = configManager.getGeneralConfig().notificationsTransparency;
        transparencySlider = new Slider(0, 0, SettingsList.WidgetWidth.BIG.getWidth(), Button.DEFAULT_HEIGHT, TRANSPARENCY_TITLE, notificationTransparency.get(), 1.0f);
        transparencySlider.setDisplayer(value -> Component.literal(Math.round(value * 100) + "%"));
        transparencySlider.setResponder(notificationTransparency::set);
        list.addElement(transparencySlider, SettingsList.WidgetWidth.BIG);

        Setting<Integer> notificationWidth = configManager.getGeneralConfig().notificationsWidth;
        widthEditBox = new IntegerEditBox(this.font, 0, 0, SettingsList.WidgetWidth.MEDIUM.getWidth(), Button.DEFAULT_HEIGHT, widthEditBox, WIDTH_TITLE, notificationWidth.get());
        widthEditBox.setResponder(value -> widthEditBox.setIntegerResponder(notificationWidth::set));
        list.addElement(widthEditBox);

        displayTimeSlider = new Slider(0, 0, SettingsList.WidgetWidth.MEDIUM.getWidth(), Button.DEFAULT_HEIGHT, DISPLAY_TIME_TITLE, Math.round(this.minecraft.options.notificationDisplayTime().get() * 10) / 10f, 0.5f, 10.0f, 0.0f);
        displayTimeSlider.setDisplayer(value -> Component.literal(Math.round(value * 10) / 10d + "x"));
        displayTimeSlider.setResponder(value -> this.minecraft.options.notificationDisplayTime().set(Math.round(value * 10) / 10d));
        displayTimeSlider.setTooltip(Tooltip.create(DISPLAY_TIME_TOOLTIP));
        list.addElement(displayTimeSlider);

        list.alignElements();
    }

    @Override
    public void onClose() {
        configManager.write(GeneralConfig.class);
        super.onClose();
    }
}
