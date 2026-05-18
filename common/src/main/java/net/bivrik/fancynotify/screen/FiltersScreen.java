package net.bivrik.fancynotify.screen;

import net.bivrik.fancynotify.config.ConfigManager;
import net.bivrik.fancynotify.config.FiltersConfig;
import net.bivrik.fancynotify.core.Common;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class FiltersScreen extends UniversalScreen {
    private static final Component TITLE = Component.literal("Filters");

    private final ConfigManager configManager;
    private final FiltersConfig filtersConfig;

    private Button backButton;

    protected FiltersScreen(Screen parent) {
        super(TITLE, parent);

        this.configManager = Common.getConfigManager();
        this.filtersConfig = this.configManager.getFiltersConfig();
    }

    @Override
    protected void init() {
        backButton = Button.builder(CommonComponents.GUI_BACK, button -> {
            configManager.write(filtersConfig);
            this.setScreen(parent);
        }).bounds(this.width / 2 - Button.BIG_WIDTH / 2, this.height - Button.DEFAULT_HEIGHT - 16, Button.BIG_WIDTH, Button.DEFAULT_HEIGHT).build();
        this.addSimpleWidget(backButton);

        var d = CycleButton.onOffBuilder(filtersConfig.isAdvancementNotificationEnabled.get()).create(0, 0, Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, Component.literal("Show Advancements"), (button, value) -> filtersConfig.isAdvancementNotificationEnabled.set(value));
        this.addSimpleWidget(d);
        var d2 = CycleButton.onOffBuilder(filtersConfig.isRecipeNotificationEnabled.get()).create(0, Button.DEFAULT_SPACING + Button.DEFAULT_HEIGHT, Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, Component.literal("Show Recipes"), (button, value) -> filtersConfig.isRecipeNotificationEnabled.set(value));
        this.addSimpleWidget(d2);
    }
}
