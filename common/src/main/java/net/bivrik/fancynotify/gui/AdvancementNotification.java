package net.bivrik.fancynotify.gui;

import net.bivrik.fancynotify.NotificationManager;
import net.bivrik.fancynotify.ResourceLocations;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

public class AdvancementNotification extends Notification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/advancement");
    private static final int MAX_LINES = 3;

    private final ItemStack icon;
    private final int backgroundOffset;
    private final boolean isChallenge;
    private final int color;

    private boolean isSoundPlayed;

    public AdvancementNotification(NotificationManager manager, DisplayInfo display) {
        super(manager);

        AdvancementType type = display.getType();
        this.setDisplay(type.getDisplayName(), display.getTitle());
        this.icon = display.getIcon();
        this.backgroundOffset = Math.min(this.messageLines.size() - 1, MAX_LINES - 1) * 9;
        this.isChallenge = type == AdvancementType.CHALLENGE;
        this.color = this.isChallenge ? new Color(255, 119, 255).getRGB() : Color.yellow.getRGB();
    }

    @Override
    public int getHeight() {
        return super.getHeight() + backgroundOffset;
    }

    @Override
    public int getLifeTimeTicks() {
        return super.getLifeTimeTicks() + 20;
    }

    @Override
    public void update(float deltaTicks) {
        super.update(deltaTicks);

        if (!isSoundPlayed && this.timeTicks >= this.animationDurationTicks - this.animationDurationTicks / 2f) {
            isSoundPlayed = true;
            if (isChallenge) {
                this.soundManager.play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1, 1));
            }
        }
    }

    @Override
    public void draw(GuiGraphics guiGraphics) {
        drawSprite(guiGraphics, BACKGROUND, 0, 0, this.getWidth(), this.getHeight());
        drawText(guiGraphics, this.title, 29, 7, color);
        for (int i = 0; i < Math.min(this.messageLines.size(), MAX_LINES); i++) {
            var line = this.messageLines.get(i);
            drawText(guiGraphics, line, 29, 18 + i * 9, -1);
        }
        if (this.messageLines.size() > MAX_LINES) {
            int index = MAX_LINES - 1;
            var line = this.messageLines.get(index);
            drawText(guiGraphics, Component.literal("..."), 29 + this.font.width(line), 18 + index * 9 , -1);
        }
        guiGraphics.renderFakeItem(icon, 8, this.getCenterY() - 8);
    }
}
