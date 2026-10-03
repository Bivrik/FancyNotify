package net.bivrik.fancynotify.gui.notification;

import net.bivrik.fancynotify.FancyNotify;
import net.bivrik.fancynotify.api.gui.icon.ItemIcon;
import net.bivrik.fancynotify.api.gui.notification.NotificationContext;
import net.bivrik.fancynotify.api.gui.notification.NotificationGraphics;
import net.bivrik.fancynotify.particle.Particle2DSetup;
import net.bivrik.fancynotify.utility.Color;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;

public class AdvancementNotification extends FancyNotification {
    private static final ResourceLocation TASK_BACKGROUND = ResourceLocations.of("notifications/task");
    private static final ResourceLocation GOAL_BACKGROUND = ResourceLocations.of("notifications/goal");
    private static final ResourceLocation CHALLENGE_BACKGROUND = ResourceLocations.of("notifications/challenge");
    private static final int TASK_COLOR = Color.YELLOW;
    private static final int GOAL_COLOR = Color.CYAN;
    private static final int CHALLENGE_COLOR = Color.create(255, 94, 209);

    private final AdvancementType type;
    private final int textColor;
    private final ResourceLocation background;

    private boolean isCelebrated;

    public AdvancementNotification(Component title, AdvancementType type, ItemStack icon) {
        super(type.getDisplayName(), title);
        setIcon(new ItemIcon(icon));

        this.type = type;
        switch (type) {
            case GOAL -> {
                this.textColor = GOAL_COLOR;
                this.background = GOAL_BACKGROUND;
            }
            case CHALLENGE -> {
                this.textColor = CHALLENGE_COLOR;
                this.background = CHALLENGE_BACKGROUND;
            }
            default -> {
                this.textColor = TASK_COLOR;
                this.background = TASK_BACKGROUND;
            }
        }
    }

    @Override
    public boolean shouldDisplay() {
        return this.filters.isAdvancementNotificationEnabled.get();
    }

    @Override
    public int getDurationTicks() {
        return super.getDurationTicks() + 30;
    }

    @Override
    public void onUpdate(NotificationContext context) {
        if (!isCelebrated && context.getTimeTicks() >= context.getAnimationDurationTicks() * 0.3f) {
            isCelebrated = true;

            Particle2DSetup.Builder setupBuilder = new Particle2DSetup.Builder(30, context.getGlobalX() + getCenterX(), context.getGlobalY() + getCenterY())
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
                    FancyNotify.getInstance().getParticleEngine().spawn(setup, 12);
                }
                case CHALLENGE -> {
                    this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1, 1));
                    Particle2DSetup setup = setupBuilder.spreadY(10)
                            .angle(-180).spreadAngle(8)
                            .speed(0).spreadSpeed(16)
                            .movementFriction(0.16f)
                            .color(CHALLENGE_COLOR).build();
                    FancyNotify.getInstance().getParticleEngine().spawn(setup, 24);
                }
            }
        }
    }

    @Override
    public void render(NotificationGraphics graphics, float partialTick) {
        graphics.sprite(background, 0, 0, getWidth(), getHeight());
        graphics.text(getTitle(), getTextOffset(), 7, textColor);
        graphics.multilineText(getWrappedMessage(), getTextOffset(), 18, -1);
        graphics.icon(getIcon(), 8, getCenterY() - 8);
    }
}
