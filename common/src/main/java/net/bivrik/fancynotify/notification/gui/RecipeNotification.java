package net.bivrik.fancynotify.notification.gui;

import net.bivrik.fancynotify.notification.ExpandableNotification;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Recipe;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecipeNotification extends ExpandableNotification {
    private static final Component TITLE = Component.translatable("recipe.toast.title");
    private static final Component MESSAGE = Component.translatable("recipe.toast.description");

    private final List<Recipe<?>> recipes = new ArrayList<>();
    private final int color = new Color(119, 0, 119).getRGB();

    public RecipeNotification(NotificationManager manager, Recipe<?> recipe) {
        super(manager, TITLE, MESSAGE);

        recipes.add(recipe);
    }

    @Override
    public boolean shouldDisplay() {
        return this.filtersConfig.isRecipeNotificationEnabled.get();
    }

    @Override
    protected void expand(ExpandableNotification notification) {
        if (notification instanceof RecipeNotification recipeNotification) {
            recipes.add(recipeNotification.recipes.get(0));
        }
    }

    private float countTemp = 0;
    @Override
    public void draw(GuiGraphics guiGraphics) {
        countTemp += 1 / 2f;
        drawBackground(guiGraphics, 0, 224);
        drawText(guiGraphics, getTitle(), getTextOffset(), 7, color);
        drawMessage(guiGraphics, getTextOffset(), 18, Color.black.getRGB());

        int orderedIndex = (int) (countTemp / Math.max(1f, (double) getLifeTimeTicks() / recipes.size()) % recipes.size());
        var recipe = recipes.get(orderedIndex);
        var stack = guiGraphics.pose();
        stack.pushPose();
        stack.scale(0.85f, 0.85f, 1.0f);
        stack.translate(0, 0, -20);
        guiGraphics.renderFakeItem(recipe.getToastSymbol(), 11, getCenterY() - 1);
        stack.popPose();
        guiGraphics.renderFakeItem(recipe.getResultItem(Objects.requireNonNull(this.minecraft.level).registryAccess()), 8, getCenterY() - 12);
    }
}
