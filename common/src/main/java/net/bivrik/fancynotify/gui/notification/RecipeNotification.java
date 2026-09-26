package net.bivrik.fancynotify.gui.notification;

import net.bivrik.fancynotify.api.Notification;
import net.bivrik.fancynotify.api.NotificationGraphics;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecipeNotification extends FancyExpandableNotification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/recipe");
    private static final Component TITLE = Component.translatable("recipe.toast.title");
    private static final Component MESSAGE = Component.translatable("recipe.toast.description");
    private static final int TITLE_COLOR = new Color(119, 0, 119).getRGB();
    private static final int MESSAGE_COLOR = Color.black.getRGB();

    private final List<RecipeHolder<?>> recipes = new ArrayList<>();

    public RecipeNotification(RecipeHolder<?> recipes) {
        super(TITLE, MESSAGE);

        this.recipes.add(recipes);
    }

    @Override
    public boolean shouldDisplay() {
        return this.filters.isRecipeNotificationEnabled.get();
    }

    @Override
    public void expand(Notification expansion) {
        RecipeNotification other = (RecipeNotification) expansion;

        recipes.addAll(other.recipes);
    }

    private float countTemp = 0;
    @Override
    public void render(NotificationGraphics graphics, float partialTick) {
        countTemp += 1 / 2f;
        graphics.sprite(BACKGROUND, 0, 0, getWidth(), getHeight());
        graphics.text(getTitle(), getTextOffset(), 7, TITLE_COLOR);
        graphics.multilineText(getWrappedMessage(), getTextOffset(), 18, MESSAGE_COLOR);

        int orderedIndex = (int) (countTemp / Math.max(1f, (double) getLifeTimeTicks() / recipes.size()) % recipes.size());
        var recipe = recipes.get(orderedIndex).value();

        GuiGraphics guiGraphics = graphics.unwrap();
        var stack = guiGraphics.pose();
        stack.pushPose();
        stack.scale(0.85f, 0.85f, 1.0f);
        stack.translate(0, 0, -20);
        guiGraphics.renderFakeItem(recipe.getToastSymbol(), 11, getCenterY() - 1);
        stack.popPose();
        guiGraphics.renderFakeItem(recipe.getResultItem(Objects.requireNonNull(this.minecraft.level).registryAccess()), 8, getCenterY() - 12);
    }
}
