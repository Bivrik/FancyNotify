package net.bivrik.fancynotify.notification.gui;

import net.bivrik.fancynotify.notification.Notification;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.particle.Particle2DSetup;
import net.bivrik.fancynotify.utility.Identifiers;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

public class AdvancementNotification extends Notification {
    private static final Identifier TASK_BACKGROUND = Identifiers.of("notifications/task");
    private static final Identifier GOAL_BACKGROUND = Identifiers.of("notifications/goal");
    private static final Identifier CHALLENGE_BACKGROUND = Identifiers.of("notifications/challenge");
    private static final Color TASK_COLOR = Color.yellow;
    private static final Color GOAL_COLOR = Color.cyan;
    private static final Color CHALLENGE_COLOR = new Color(255, 94, 209);

    private final AdvancementType type;
    private final ItemStack icon;
    private final int textColor;
    private final Identifier background;

    private boolean isCelebrated;

    public AdvancementNotification(NotificationManager manager, Component title, AdvancementType type, ItemStack icon) {
        super(manager, type.getDisplayName(), title);

        this.type = type;
        this.icon = icon;
        switch (type) {
            case GOAL -> {
                this.textColor = GOAL_COLOR.getRGB();
                this.background = GOAL_BACKGROUND;
            }
            case CHALLENGE -> {
                this.textColor = CHALLENGE_COLOR.getRGB();
                this.background = CHALLENGE_BACKGROUND;
            }
            default -> {
                this.textColor = TASK_COLOR.getRGB();
                this.background = TASK_BACKGROUND;
            }
        }
    }

    @Override
    public boolean shouldDisplay() {
        return this.filtersConfig.isAdvancementNotificationEnabled.get();
    }

    @Override
    public int getLifeTimeTicks() {
        return super.getLifeTimeTicks() + 30;
    }

    @Override
    public void onUpdate() {
        int animationDurationTicks = this.generalConfig.animationDuration.get();
        if (!isCelebrated && this.timeTicks >= animationDurationTicks * 0.3f) {
            isCelebrated = true;

            Particle2DSetup.Builder setupBuilder = new Particle2DSetup.Builder(30, this.globalX + getWidth() / 2.0f, this.globalY + getHeight() / 2.0f)
                    .spreadX(5)
                    .startRotation(-90).spreadStartRotation(90)
                    .endRotation(90).spreadEndRotation(90);

            switch (type) {
                case TASK -> {
                    this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 1, 1));
                }
                case GOAL -> {
                    this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.FIREWORK_ROCKET_TWINKLE, 1, 1));
                    Particle2DSetup setup = setupBuilder.spreadY(5)
                            .angle(0).spreadAngle(360)
                            .speed(1.5f).spreadSpeed(1.5f)
                            .movementFriction(0.02f)
                            .color(GOAL_COLOR).build();
                    this.particleEngine.spawn(setup, 12);
                }
                case CHALLENGE -> {
                    this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1, 1));
                    Particle2DSetup setup = setupBuilder.spreadY(10)
                            .angle(-180).spreadAngle(8)
                            .speed(0).spreadSpeed(16)
                            .movementFriction(0.16f)
                            .color(CHALLENGE_COLOR).build();
                    this.particleEngine.spawn(setup, 24);
                }
            }
        }
    }

    @Override
    public void draw(GuiGraphicsExtractor GuiGraphicsExtractor) {
        drawSprite(GuiGraphicsExtractor, background, 0, 0, getWidth(), getHeight());
        drawText(GuiGraphicsExtractor, getTitle(), getTextOffset(), 7, textColor);
        drawMessage(GuiGraphicsExtractor, getTextOffset(), 18, -1);
        GuiGraphicsExtractor.fakeItem(icon, 8, getCenterY() - 8);
    }
}
