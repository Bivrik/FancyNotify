package net.bivrik.fancynotify.gui.notification;

import net.bivrik.fancynotify.api.Icon;
import net.bivrik.fancynotify.api.Notification;
import net.bivrik.fancynotify.api.NotificationContext;
import net.bivrik.fancynotify.api.NotificationGraphics;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.client.Minecraft;
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

    public RecipeNotification(RecipeHolder<?> recipe) {
        super(TITLE, MESSAGE);
        setIcon(new RecipeIcon(Minecraft.getInstance(), recipe));
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
    public void render(NotificationGraphics graphics, float partialTick) {
        graphics.sprite(BACKGROUND, 0, 0, getWidth(), getHeight());
        graphics.text(getTitle(), getTextOffset(), 7, TITLE_COLOR);
        graphics.multilineText(getWrappedMessage(), getTextOffset(), 18, MESSAGE_COLOR);
        graphics.icon(getIcon(), 8, getCenterY() - 12);
    }

    private static final class RecipeIcon implements Icon {
        private final Minecraft minecraft;
        private final List<RecipeHolder<?>> recipes = new ArrayList<>();

        private float count = 0;
        private int orderedIndex = 0;

        public RecipeIcon(Minecraft minecraft, RecipeHolder<?> recipe) {
            this.minecraft = minecraft;
            recipes.add(recipe);
        }

        public void addRecipes(List<RecipeHolder<?>> recipes) {
            this.recipes.addAll(recipes);
        }

        public List<RecipeHolder<?>> getRecipes() {
            return recipes;
        }

        @Override
        public void update(NotificationContext context) {
            count += 1 / 2f;
            orderedIndex = (int) (count / Math.max(1f, (double) context.getTimeTicks() / recipes.size()) % recipes.size());
        }

        @Override
        public void draw(NotificationGraphics graphics, int x, int y) {
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
