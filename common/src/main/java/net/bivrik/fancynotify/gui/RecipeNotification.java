package net.bivrik.fancynotify.gui;

import net.bivrik.fancynotify.NotificationManager;
import net.bivrik.fancynotify.ResourceLocations;
import net.bivrik.fancynotify.core.Logger;
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
    private static final Component TITLE_TEXT = Component.translatable("recipe.toast.title");
    private static final Component DESCRIPTION_TEXT = Component.translatable("recipe.toast.description");

    private final List<RecipeHolder<?>> recipes = new ArrayList<>();
    private final int color = new Color(119, 0, 119).getRGB();

    public RecipeNotification(NotificationManager manager, RecipeHolder<?> recipe) {
        super(manager);

        this.setDisplay(TITLE_TEXT, DESCRIPTION_TEXT);
        recipes.add(recipe);
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
        drawSprite(guiGraphics, BACKGROUND, 0, 0, this.getWidth(), this.getHeight());
        drawText(guiGraphics, this.title, 29, 7, color);
        drawText(guiGraphics, this.message, 29, 18, Color.black.getRGB());
        int orderedIndex = (int) (countTemp / Math.max(1f, (double) this.getLifeTimeTicks() / recipes.size()) % recipes.size());
        var recipe = recipes.get(orderedIndex).value();
        var stack = guiGraphics.pose();
        stack.pushPose();
        stack.scale(0.825f, 0.825f, 1.0f);
        stack.translate(0, 0, -20);
        guiGraphics.renderFakeItem(recipe.getToastSymbol(), 9, getCenterY());
        stack.popPose();
        guiGraphics.renderFakeItem(recipe.getResultItem(Objects.requireNonNull(this.notificationManager.getMinecraft().level).registryAccess()), 6, getCenterY() - 12);
    }
}
