package net.bivrik.fancynotify.screen;

import net.bivrik.fancynotify.IntegerEditBox;
import net.bivrik.fancynotify.NotificationManager;
import net.bivrik.fancynotify.Slider;
import net.bivrik.fancynotify.config.ConfigManager;
import net.bivrik.fancynotify.config.GeneralConfig;
import net.bivrik.fancynotify.config.Setting;
import net.bivrik.fancynotify.core.Common;
import net.bivrik.fancynotify.event.NotificationWidthChangedEvent;
import net.bivrik.fancynotify.gui.SystemNotification;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Random;

public class SettingsScreen extends UniversalScreen {
    private static final Component TITLE = Component.literal("Settings");
    private static final Component TRANSPARENCY_TITLE = Component.literal("Notifications Transparency");
    private static final Component WIDTH_TITLE = Component.literal("Notifications Width");
    private static final Component DISPLAY_TIME_TITLE = Component.literal("Notifications Time");
    private static final Component DISPLAY_TIME_TOOLTIP = Component.translatable("options.notifications.display_time.tooltip");;
    private static final Component ORIENTATION_TITLE = Component.literal("Orientation");
    private static final Component ORIENTATION_TOOLTIP = Component.literal("Represents how notifications will be shown");
    private static final Component ANCHOR_TITLE = Component.literal("Anchor");
    private static final Component ANCHOR_TOOLTIP = Component.literal("Represents position where notifications will be shown");

    private final ConfigManager configManager;
    private final NotificationManager notificationManager;

    private Button backButton;
    private Slider transparencySlider;
    private Slider paddingSlider;
    private Slider maxAmountSlider;
    private IntegerEditBox widthEditBox;
    private Slider displayTimeSlider;
    private CycleButton<GeneralConfig.Orientation> orientationCycleButton;
    private CycleButton<GeneralConfig.Anchor> anchorCycleButton;
    private CycleButton<Boolean> debugCycleButton;
    private CycleButton<GeneralConfig.Animation> animationCycleButton;
    private Slider animationDurationSlider;
    private Button createDummyButton;

    protected SettingsScreen(Screen parent) {
        super(TITLE, parent);

        this.configManager = Common.getConfigManager();
        this.notificationManager = Common.getNotificationManager();
    }

    @Override
    protected void init() {
        backButton = Button.builder(CommonComponents.GUI_BACK, button -> this.onClose())
                .bounds(this.width / 2 - Button.DEFAULT_WIDTH - 5, this.height - Button.DEFAULT_HEIGHT - 6, 180, Button.DEFAULT_HEIGHT).build();
        this.addSimpleWidget(backButton);

        createDummyButton = Button.builder(Component.literal("Send Dummy"), button -> sendDummy())
                .bounds(this.width / 2 + 30 + 5, this.height - Button.DEFAULT_HEIGHT - 6, Button.SMALL_WIDTH, Button.DEFAULT_HEIGHT)
                .build();
        this.addSimpleWidget(createDummyButton);

        SettingsList list = new SettingsList(this.minecraft, this.width, this.height - 64 - 2, 32, 25, this);
        this.addSimpleWidget(list);

        Setting<Float> notificationTransparency = configManager.getGeneralConfig().notificationsTransparency;
        transparencySlider = new Slider(0, 0, SettingsList.WidgetWidth.BIG.getWidth(), Button.DEFAULT_HEIGHT, TRANSPARENCY_TITLE, notificationTransparency.get(), 0.3f, 1.0f);
        transparencySlider.setDisplayer(value -> Component.literal(Math.round(value * 100) + "%"));
        transparencySlider.setResponder(notificationTransparency::set);
        list.addElement(transparencySlider, SettingsList.WidgetWidth.BIG);

        Setting<GeneralConfig.Orientation> orientation = configManager.getGeneralConfig().orientation;
        orientationCycleButton = CycleButton.builder(GeneralConfig.Orientation::getDisplayName)
                .withValues(GeneralConfig.Orientation.values())
                .withInitialValue(orientation.get())
                .withTooltip(value -> Tooltip.create(ORIENTATION_TOOLTIP))
                .create(0, 0, Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, ORIENTATION_TITLE, (button, value) -> orientation.set(value));
        list.addElement(orientationCycleButton);

        Setting<GeneralConfig.Anchor> anchor = configManager.getGeneralConfig().anchor;
        anchorCycleButton = CycleButton.builder(GeneralConfig.Anchor::getDisplayName)
                .withValues(GeneralConfig.Anchor.values())
                .withInitialValue(anchor.get())
                .withTooltip(value -> Tooltip.create(ANCHOR_TOOLTIP))
                .create(0, 0, Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, ANCHOR_TITLE, (button, value) -> anchor.set(value));
        list.addElement(anchorCycleButton);

        Setting<Integer> notificationWidth = configManager.getGeneralConfig().notificationsWidth;
        widthEditBox = new IntegerEditBox(this.font, 0, 0, SettingsList.WidgetWidth.MEDIUM.getWidth(), Button.DEFAULT_HEIGHT, widthEditBox, WIDTH_TITLE, notificationWidth.get());
        widthEditBox.setResponder(value -> widthEditBox.setIntegerResponder(iValue -> notificationWidth.set(Math.clamp(iValue, 20, this.width - configManager.getGeneralConfig().padding.get() * 2))));
        list.addElement(widthEditBox);

        Setting<Integer> padding = configManager.getGeneralConfig().padding;
        paddingSlider = new Slider(0, 0, SettingsList.WidgetWidth.MEDIUM.getWidth(), Button.DEFAULT_HEIGHT, Component.literal("Padding"), padding.get(), 8);
        paddingSlider.setDisplayer(value -> Component.literal(String.valueOf(value.intValue())));
        paddingSlider.setResponder(value -> padding.set(value.intValue()));
        list.addElement(paddingSlider);

        Setting<GeneralConfig.Animation> animation = configManager.getGeneralConfig().animation;
        animationCycleButton = CycleButton.builder(GeneralConfig.Animation::getDisplayName)
                .withValues(GeneralConfig.Animation.values())
                .withInitialValue(animation.get())
                .withTooltip(value -> Tooltip.create(Component.literal("Affects how notifications appear")))
                .create(0, 0, SettingsList.WidgetWidth.BIG.getWidth(), Button.DEFAULT_HEIGHT, Component.literal("Animation"), (button, value) -> animation.set(value));
        list.addElement(animationCycleButton, SettingsList.WidgetWidth.BIG);

        Setting<Integer> maxAmount = configManager.getGeneralConfig().maxAmount;
        maxAmountSlider = new Slider(0, 0, SettingsList.WidgetWidth.MEDIUM.getWidth(), Button.DEFAULT_HEIGHT, Component.literal("Max Amount"), maxAmount.get(), 1, 12);
        maxAmountSlider.setDisplayer(value -> Component.literal(String.valueOf(value.intValue())));
        maxAmountSlider.setResponder(value -> maxAmount.set(value.intValue()));
        list.addElement(maxAmountSlider);

        displayTimeSlider = new Slider(0, 0, SettingsList.WidgetWidth.MEDIUM.getWidth(), Button.DEFAULT_HEIGHT, DISPLAY_TIME_TITLE, Math.round(this.minecraft.options.notificationDisplayTime().get() * 10) / 10f, 0.5f, 10.0f, 0.0f);
        displayTimeSlider.setDisplayer(value -> Component.literal(Math.round(value * 10) / 10d + "x"));
        displayTimeSlider.setResponder(value -> this.minecraft.options.notificationDisplayTime().set(Math.round(value * 10) / 10d));
        displayTimeSlider.setTooltip(Tooltip.create(DISPLAY_TIME_TOOLTIP));
        list.addElement(displayTimeSlider);

        Setting<Integer> animationDuration = configManager.getGeneralConfig().animationDuration;
        animationDurationSlider = new Slider(0, 0, SettingsList.WidgetWidth.MEDIUM.getWidth(), Button.DEFAULT_HEIGHT, Component.literal("Animation Duration"), animationDuration.get(), 5, 40);
        animationDurationSlider.setDisplayer(value -> Component.literal(value.intValue() + " ticks"));
        animationDurationSlider.setResponder(value -> animationDuration.set(value.intValue()));
        list.addElement(animationDurationSlider);

        Setting<Boolean> debug = configManager.getGeneralConfig().debug;
        debugCycleButton = CycleButton.onOffBuilder()
                .withInitialValue(debug.get())
                .withTooltip(value -> Tooltip.create(Component.literal("Option for development")))
                .create(0, 0, Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, Component.literal("Debug"), (button, value) -> debug.set(value));
        list.addElement(debugCycleButton);

        list.alignElements();
    }

    private void sendDummy() {
        List<SystemNotification> notifications = List.of(
                new SystemNotification(notificationManager, SystemNotification.Identifier.PERIODIC_NOTIFICATION, Component.literal("Bivrik is lazy"), Component.literal("WHO WROTE THAT?!")),
                new SystemNotification(notificationManager, SystemNotification.Identifier.CHUNK_SAVE_FAILURE, Component.literal("Some title"), Component.literal("Some error message")),
                new SystemNotification(notificationManager, SystemNotification.Identifier.LOW_DISK_SPACE, Component.literal("Low disk space"), Component.literal("Oh no! Your disk is full of stuff! You cannot save or smth idk")),
                new SystemNotification(notificationManager, SystemNotification.Identifier.UNSECURE_SERVER_WARNING, Component.literal("Unsecure"), Component.literal("Meow meow meow ruff ruff wtf-")),
                new SystemNotification(notificationManager, SystemNotification.Identifier.PACK_LOAD_FAILURE, Component.literal("Resource pack failure"), Component.literal("You're a failure")),
                new SystemNotification(notificationManager, SystemNotification.Identifier.WORLD_ACCESS_FAILURE, Component.literal("No worlds"), Component.literal("*no worlds?* hehe"))
        );

        Random random = new Random();
        notificationManager.add(notifications.get(random.nextInt(notifications.size())));
    }

    @Override
    public void onClose() {
        configManager.write(GeneralConfig.class);
        this.minecraft.options.save();
        super.onClose();
    }
}
