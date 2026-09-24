package net.bivrik.fancynotify.notification;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.bivrik.fancynotify.FancyNotify;
import net.bivrik.fancynotify.api.Notification;
import net.bivrik.fancynotify.api.NotificationContext;
import net.bivrik.fancynotify.config.ConfigManager;
import net.bivrik.fancynotify.config.data.GeneralConfig;
import net.bivrik.fancynotify.eventbus.SubscribeEvent;
import net.bivrik.fancynotify.eventbus.event.NotificationWidthChangedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;

public class NotificationHost implements NotificationStateMachine.Listener {
    private final NotificationStateMachine stateMachine;
    private final NotificationAnimator animator;

    protected final Minecraft minecraft;
    protected final GeneralConfig config;
    protected final Notification content;
    protected float timeTicks = 0;
    protected float offsetTicks = 0;

    public NotificationHost(Notification content, Minecraft minecraft, ConfigManager configManager) {
        this.minecraft = minecraft;
        this.config = configManager.getGeneralConfig();

        this.content = content;
        this.content.setPreferableWidth(this.config.notificationsWidth.get());

        this.stateMachine = new NotificationStateMachine(minecraft, this);
        this.animator = this.config.getAnimator();
    }

    @SubscribeEvent
    public void onNotificationWidthChanged(NotificationWidthChangedEvent event) {
        content.setPreferableWidth(event.getWidth());
    }

    public final int getWidth() {
        return content.getWidth();
    }

    public final int getHeight() {
        return content.getHeight();
    }

    public final Object getId() {
        return content.getId();
    }

    public final boolean shouldDisplay() {
        return content.shouldDisplay();
    }

    protected final int getLifeTimeTicks() {
        return content.getLifeTimeTicks();
    }

    public final boolean shouldRemove() {
        return stateMachine.isInState(NotificationState.REMOVAL);
    }

    public final void forceHide() {
        stateMachine.hide();
    }

    public boolean tryMerge(Notification other) {
        return false;
    }

    @Override
    public void onShowing() {
        FancyNotify.EVENT_BUS.register(this);
        minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_IN, 1.0f, 1.0f));
        content.onShowing();
    }

    @Override
    public void onVisible() {
        content.onVisible();
    }

    @Override
    public void onHiding() {
        minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_OUT, 1.0f, 1.0f));
        content.onHiding();
    }

    @Override
    public void onRemoval() {
        FancyNotify.EVENT_BUS.unregister(this);
        content.onRemoval();
    }

    public void update(float deltaTicks, float globalX, float globalY) {
        timeTicks += deltaTicks;

        stateMachine.update(timeTicks, offsetTicks, config.animationDuration.get(), getLifeTimeTicks());
        animator.update(timeTicks, stateMachine.getState(), stateMachine.getTimingTicks(), getWidth(), getHeight(), config.animationDuration.get());

        NotificationContext context = new NotificationContextImpl(globalX, globalY, timeTicks, config.animationDuration.get());
        content.update(context);
    }

    public final void render(GuiGraphics guiGraphics, float partialTick) {
        if (stateMachine.isInState(NotificationState.HIDDEN) || stateMachine.isInState(NotificationState.REMOVAL)) {
            return;
        }

        float halfWidth = getWidth() / 2.0f;
        float halfHeight = getHeight() / 2.0f;

        var notificationGraphics = new NotificationGraphicsImpl(guiGraphics, minecraft.font, animator);

        PoseStack stack = guiGraphics.pose();
        stack.pushPose();
        stack.translate(halfWidth, halfHeight, 0);
        stack.scale(animator.getScaleX(), animator.getScaleY(), 1);
        stack.translate(-halfWidth, -halfHeight, 0);
        stack.rotateAround(Axis.ZP.rotation(animator.getRotation()), halfWidth, halfHeight, 0);
        stack.translate(animator.getX(), animator.getY(), 0);
        content.draw(notificationGraphics, partialTick);
        stack.popPose();
    }
}
