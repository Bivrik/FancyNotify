package net.bivrik.fancynotify.compat.spectrum.notification;

import net.bivrik.fancynotify.api.Icon;
import net.bivrik.fancynotify.api.NotificationContext;
import net.bivrik.fancynotify.api.NotificationGraphics;
import net.bivrik.fancynotify.gui.notification.FancyNotification;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.List;

public class UnlockedRecipeNotification extends FancyNotification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/spectrum/recipe");
    private static final int TITLE_COLOR = new Color(115, 40, 244).getRGB();
    private static final int MESSAGE_COLOR = new Color(35, 35, 35).getRGB();

    private final SoundEvent sound;

    private boolean isSoundPlayed;

    public UnlockedRecipeNotification(Component title, Component message, List<ItemStack> icons, SoundEvent sound) {
        super(title, message);
        setIcon(new UnlockedRecipeIcon(icons));

        this.sound = sound;
    }

    @Override
    public boolean shouldDisplay() {
        return this.filters.isSpectrumUnlockedRecipeNotificationEnabled.get();
    }

    @Override
    public void onUpdate(NotificationContext context) {
        if (!isSoundPlayed && context.getTimeTicks() > 0) {
            isSoundPlayed = true;
            this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(sound, 1.0f, 0.6f));
        }
    }

    @Override
    public void render(NotificationGraphics graphics, float partialTick) {
        graphics.sprite(BACKGROUND, 0, 0, getWidth(), getHeight());
        graphics.text(getTitle(), getTextOffset(), 7, TITLE_COLOR);
        graphics.multilineText(getWrappedMessage(), getTextOffset(), 18, MESSAGE_COLOR);
        graphics.icon(getIcon(), 8, getCenterY() - 8);
    }

    private static final class UnlockedRecipeIcon implements Icon {
        private final List<ItemStack> recipeResults;

        private float count = 0;
        private int orderedIndex = 0;

        public UnlockedRecipeIcon(List<ItemStack> recipeResults) {
            this.recipeResults = recipeResults;
        }

        @Override
        public void update(NotificationContext context) {
            count += 1 / 2f;
            orderedIndex = (int) (count / Math.max(1f, (double) context.getTimeTicks() / recipeResults.size()) % recipeResults.size());
        }

        @Override
        public void draw(NotificationGraphics graphics, int x, int y) {
            graphics.unwrap().renderFakeItem(recipeResults.get(orderedIndex), x, y);
        }
    }
}
