package net.bivrik.fancynotify.gui.notification;

import net.bivrik.fancynotify.FancyNotify;
import net.bivrik.fancynotify.api.gui.icon.SpriteIcon;
import net.bivrik.fancynotify.api.gui.notification.NotificationContext;
import net.bivrik.fancynotify.api.gui.notification.NotificationGraphics;
import net.bivrik.fancynotify.particle.Particle2DSetup;
import net.bivrik.fancynotify.utility.Color;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

public class DayCounterNotification extends FancyNotification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/music");
    private static final ResourceLocation ICON = ResourceLocations.of("icons/music");
    private static final int TITLE_COLOR = Color.CYAN;

    private boolean isCelebrated;

    public DayCounterNotification(int dayCount) {
        super(Component.literal("New milestone achieved!"),
                Component.literal("Welcome to " + dayCount + "th day!"));
        setIcon(new SpriteIcon(ICON, 21, 21));
    }

    @Override
    public boolean shouldDisplay() {
        return this.filters.isDayCounterNotificationEnabled.get();
    }

    @Override
    public int getDurationTicks() {
        return 200;
    }

    @Override
    protected void onUpdate(NotificationContext context) {
        if (!isCelebrated && context.getElapsedTicks() >= context.getAnimationDurationTicks() * 0.35f) {
            isCelebrated = true;

            SoundManager soundManager = this.minecraft.getSoundManager();
            soundManager.play(SimpleSoundInstance.forUI(SoundEvents.FIREWORK_ROCKET_BLAST, 1.0f, 0.8f));
            soundManager.play(SimpleSoundInstance.forUI(SoundEvents.FIREWORK_ROCKET_TWINKLE, 1.0f, 0.9f));

            Particle2DSetup setup = new Particle2DSetup.Builder(40, context.getGlobalX() + getCenterX(), context.getGlobalY() + getHeight() - 5)
                    .spreadX(getWidth() / 3.0f)
                    .startRotation(-90).spreadStartRotation(90)
                    .endRotation(90).spreadEndRotation(90)
                    .angle(-90).spreadAngle(45)
                    .speed(1.3f).spreadSpeed(1.3f)
                    .movementFriction(0.03f)
                    .color(Color.create(25, 200, 200))
                    .build();

            FancyNotify.getInstance().getParticleEngine().spawn(setup, 23);
        }
    }

    @Override
    public void onRender(NotificationGraphics graphics) {
        graphics.sprite(BACKGROUND, 0, 0, getWidth(), getHeight());
        graphics.text(getTitle(), getTextLeftPadding(), 7, TITLE_COLOR);
        graphics.multilineText(getMessageLines(), getTextLeftPadding(), 18, -1);
        graphics.icon(getIcon(), 4, getCenterY() - 10);
    }
}
