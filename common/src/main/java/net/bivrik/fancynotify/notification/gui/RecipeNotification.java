package net.bivrik.fancynotify.notification.gui;

import net.bivrik.fancynotify.notification.ExpandableNotification;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecipeNotification extends ExpandableNotification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/recipe");
    private static final Component TITLE = Component.translatable("recipe.toast.title");
    private static final Component MESSAGE = Component.translatable("recipe.toast.description");

    private final List<RecipeHolder<?>> recipes = new ArrayList<>();
    private final int color = new Color(119, 0, 119).getRGB();

    public RecipeNotification(NotificationManager manager, RecipeHolder<?> recipe) {
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
            recipes.add(recipeNotification.recipes.getFirst());
        }
    }

    private float countTemp = 0;
    @Override
    public void draw(GuiGraphics guiGraphics) {
        countTemp += 1 / 2f;
        drawSprite(guiGraphics, BACKGROUND, 0, 0, getWidth(), getHeight());
        drawText(guiGraphics, getTitle(), getTextOffset(), 7, color);
        drawMessage(guiGraphics, getTextOffset(), 18, Color.black.getRGB());

        int orderedIndex = (int) (countTemp / Math.max(1f, (double) getLifeTimeTicks() / recipes.size()) % recipes.size());
        var recipe = recipes.get(orderedIndex).value();
        var stack = guiGraphics.pose();
        stack.pushPose();
        stack.scale(0.85f, 0.85f, 1.0f);
        stack.translate(0, 0, -20);
        guiGraphics.renderFakeItem(recipe.getToastSymbol(), 11, getCenterY() - 1);
        stack.popPose();
        guiGraphics.renderFakeItem(recipe.getResultItem(Objects.requireNonNull(this.minecraft.level).registryAccess()), 8, getCenterY() - 12);
    }
}
