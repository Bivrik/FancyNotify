package net.bivrik.fancynotify.gui.notification;

import net.bivrik.fancynotify.api.gui.icon.Icon;
import net.bivrik.fancynotify.api.gui.notification.Notification;
import net.bivrik.fancynotify.api.gui.notification.NotificationGraphics;
import net.bivrik.fancynotify.utility.Color;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecipeNotification extends FancyExpandableNotification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/recipe");
    private static final Component TITLE = Component.translatable("recipe.toast.title");
    private static final Component MESSAGE = Component.translatable("recipe.toast.description");
    private static final int TITLE_COLOR = Color.create(119, 0, 119);
    private static final int MESSAGE_COLOR = Color.BLACK;

    public RecipeNotification(RecipeHolder<?> recipe) {
        super(TITLE, MESSAGE);
        setIcon(new RecipeIcon(this.minecraft, recipe, getDurationTicks()));
    }

    @Override
    public boolean shouldDisplay() {
        return this.filters.isRecipeNotificationEnabled.get();
    }

    @Override
    public void expand(Notification expansion) {
        RecipeNotification other = (RecipeNotification) expansion;

        RecipeIcon icon = (RecipeIcon) getIcon();
        RecipeIcon otherIcon = (RecipeIcon) other.getIcon();

        icon.addRecipes(otherIcon.getRecipes());
    }

    @Override
    public void render(NotificationGraphics graphics) {
        graphics.sprite(BACKGROUND, 0, 0, getWidth(), getHeight());
        graphics.text(getTitle(), getTextOffset(), 7, TITLE_COLOR);
        graphics.multilineText(getWrappedMessage(), getTextOffset(), 18, MESSAGE_COLOR);
        graphics.icon(getIcon(), 8, getCenterY() - 12);
    }

    private static final class RecipeIcon implements Icon {
        private final Minecraft minecraft;
        private final List<RecipeHolder<?>> recipes = new ArrayList<>();
        private final float durationTicks;

        private float count = 0;

        public RecipeIcon(Minecraft minecraft, RecipeHolder<?> recipe, float durationTicks) {
            this.minecraft = minecraft;
            this.recipes.add(recipe);
            this.durationTicks = durationTicks;
        }

        public void addRecipes(List<RecipeHolder<?>> recipeExpansion) {
            recipes.addAll(recipeExpansion);
        }

        public List<RecipeHolder<?>> getRecipes() {
            return recipes;
        }

        @Override
        public void draw(NotificationGraphics graphics, int x, int y) {
            count += 1 / 2.0f;
            int orderedIndex = (int) (count / Math.max(1.0f, durationTicks / recipes.size()) % recipes.size());
            var recipe = recipes.get(orderedIndex).value();
            GuiGraphics guiGraphics = graphics.unwrap();
            var stack = guiGraphics.pose();
            stack.pushPose();
            stack.scale(0.85f, 0.85f, 1.0f);
            stack.translate(0, 0, -20);
            guiGraphics.renderFakeItem(recipe.getToastSymbol(), x + 3, y + 11);
            stack.popPose();
            guiGraphics.renderFakeItem(recipe.getResultItem(Objects.requireNonNull(minecraft.level).registryAccess()), x, y);
        }
    }
}
