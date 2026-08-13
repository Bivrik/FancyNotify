package net.bivrik.fancynotify.notification;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.bivrik.fancynotify.config.ConfigManager;
import net.bivrik.fancynotify.config.FiltersConfig;
import net.bivrik.fancynotify.config.GeneralConfig;
import net.bivrik.fancynotify.FancyNotify;
import net.bivrik.fancynotify.core.Log;
import net.bivrik.fancynotify.eventbus.event.NotificationWidthChangedEvent;
import net.bivrik.fancynotify.eventbus.SubscribeEvent;
import net.bivrik.fancynotify.particle.Particle2DEngine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class Notification implements NotificationStateMachine.Listener {
    private static final Object NO_ID = new Object();

    private Component title;
    private Component message;
    private List<FormattedCharSequence> wrappedMessage;
    private int minWidth;
    private int width;

    private final NotificationStateMachine stateMachine;
    private final NotificationAnimator animator;
    protected final Minecraft minecraft;
    protected final Particle2DEngine particleEngine;
    protected final FiltersConfig filtersConfig;
    protected final GeneralConfig generalConfig;

    protected float timeTicks = 0;
    protected float offsetTicks = 0;

    protected float globalX;
    protected float globalY;

    public Notification(NotificationManager manager, @NotNull Component title, @Nullable Component message) {
        Minecraft minecraft = manager.getMinecraft();
        ConfigManager configManager = manager.getConfigManager();

        this.minecraft = minecraft;
        this.filtersConfig = configManager.getFiltersConfig();
        this.generalConfig = configManager.getGeneralConfig();
        this.particleEngine = manager.getParticleEngine();
        this.stateMachine = new NotificationStateMachine(minecraft, this);
        this.animator = this.generalConfig.getAnimator();
        this.width = this.generalConfig.notificationsWidth.get();

        setDisplay(title, message);
    }

    protected final void setDisplay(@NotNull Component title, @Nullable Component message) {
        Component notNullMessage = message == null ? Component.empty() : message;
        this.title = title;
        this.minWidth = minecraft.font.width(title) + getTextOffset() + 7;
        this.message = notNullMessage;
        this.wrappedMessage = getWrappedText(notNullMessage);
    }

    private List<FormattedCharSequence> getWrappedText(@NotNull Component text) {
        return minecraft.font.split(text, getWidth() - 36);
    }

    @SubscribeEvent
    public void onNotificationWidthChanged(NotificationWidthChangedEvent event) {
        width = event.getWidth();
        wrappedMessage = getWrappedText(message);
        Log.info("New width is " + width);
    }

    protected final Component getTitle() {
        return title;
    }

    protected final Component getMessage() {
        return message;
    }

    protected final List<FormattedCharSequence> getWrappedMessage() {
        return new ArrayList<>(wrappedMessage);
    }

    public int getWidth() {
        return Math.max(minWidth, width);
    }

    public int getHeight() {
        return 23 + wrappedMessage.size() * 9;
    }

    protected int getCenterY() {
        return getHeight() / 2;
    }

    protected int getTextOffset() {
        return 29;
    }

    public Object getId() {
        return NO_ID;
    }

    public boolean tryMerge(Notification notification) {
        return false;
    }

    public boolean shouldDisplay() {
        return true;
    }

    public boolean shouldRemove() {
        return stateMachine.isInState(NotificationState.REMOVAL);
    }

    protected int getLifeTimeTicks() {
        return 140;
    }

    @Override
    public void onShowing() {
        FancyNotify.EVENT_BUS.register(this);
        minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_IN, 1, 1));
    }

    @Override
    public void onHiding() {
        minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_OUT, 1, 1));
    }

    @Override
    public void onRemoval() {
        FancyNotify.EVENT_BUS.unregister(this);
    }

    public void hide() {
        stateMachine.hide();
    }

    public void update(float deltaTicks, float globalX, float globalY) {
        timeTicks += deltaTicks;

        this.globalX = globalX;
        this.globalY = globalY;

        stateMachine.update(timeTicks, offsetTicks, generalConfig.animationDuration.get(), getLifeTimeTicks());
        animator.update(timeTicks, stateMachine.getState(), stateMachine.getTimingTicks(), getWidth(), getHeight(), generalConfig.animationDuration.get());

        onUpdate();
    }

    protected void onUpdate() {}

    public final void render(GuiGraphics guiGraphics) {
        if (stateMachine.isInState(NotificationState.HIDDEN) || stateMachine.isInState(NotificationState.REMOVAL)) {
            return;
        }

        float halfWidth = getWidth() / 2.0f;
        float halfHeight = getHeight() / 2.0f;
        PoseStack stack = guiGraphics.pose();
        stack.pushPose();
        stack.translate(halfWidth, halfHeight, 0);
        stack.scale(animator.getScaleX(), animator.getScaleY(), 1);
        stack.translate(-halfWidth, -halfHeight, 0);
        stack.rotateAround(Axis.ZP.rotation(animator.getRotation()), halfWidth, halfHeight, 0);
        stack.translate(animator.getX(), animator.getY(), 0);
        draw(guiGraphics);
        stack.popPose();
    }

    protected abstract void draw(GuiGraphics guiGraphics);

    protected void drawSprite(GuiGraphics guiGraphics, ResourceLocation sprite, int x, int y, int width, int height) {
        if (animator.getAlpha() == 1) {
            guiGraphics.blitSprite(sprite, x, y, width, height);
            return;
        }

        RenderSystem.enableBlend();
        guiGraphics.setColor(1, 1, 1, animator.getAlpha());
        guiGraphics.blitSprite(sprite, x, y, width, height);
        guiGraphics.setColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
    }

    protected void drawTexture(GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int width, int height, int textureWidth, int textureHeight, int uOffset, int vOffset, int uWidth, int vHeight) {
        if (animator.getAlpha() == 1) {
            guiGraphics.blit(texture, x, y, width, height, uOffset, vOffset, uWidth, vHeight, textureWidth, textureHeight);
            return;
        }

        RenderSystem.enableBlend();
        guiGraphics.setColor(1, 1, 1, animator.getAlpha());
        guiGraphics.blit(texture, x, y, width, height, uOffset, vOffset, uWidth, vHeight, textureWidth, textureHeight);
        guiGraphics.setColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
    }

    protected void drawTexture(GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int width, int height, int textureWidth, int textureHeight, int uOffset, int vOffset) {
        drawTexture(guiGraphics, texture, x, y, width, height, textureWidth, textureHeight, uOffset, vOffset, width, height);
    }

    protected void drawTexture(GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int width, int height, int textureWidth, int textureHeight) {
        drawTexture(guiGraphics, texture, x, y, width, height, textureWidth, textureHeight, 0, 0, width, height);
    }

    protected void drawText(GuiGraphics guiGraphics, FormattedCharSequence text, int x, int y, int color) {
        if (animator.getAlpha() == 1) {
            guiGraphics.drawString(minecraft.font, text, x, y, color, false);
            return;
        }

        // Am I not understanding something
        // or why is it so complicated?
        // I mean... why doesn't it work
        // as intended from the start???
        // Why without all of this there is a bug,
        // when using guiGraphics.setColor(),
        // it makes all the tooltips with the same color?
        MultiBufferSource.BufferSource isolatedBuffer = MultiBufferSource.immediate(new ByteBufferBuilder(256));
        int iAlpha = Math.max((int) (animator.getAlpha() * 255), 25);
        int alphaColor = (iAlpha << 24) | (color & 0x00FFFFFF);
        RenderSystem.enableBlend();
        minecraft.font.drawInBatch(
                text, x, y, alphaColor, false,
                guiGraphics.pose().last().pose(),
                isolatedBuffer,
                Font.DisplayMode.NORMAL,
                0, 15728880
        );
        isolatedBuffer.endBatch();
        RenderSystem.disableBlend();
    }

    protected void drawText(GuiGraphics guiGraphics, Component text, int x, int y, int color) {
        drawText(guiGraphics, text.getVisualOrderText(), x, y, color);
    }

    protected void drawMessage(GuiGraphics guiGraphics, int x, int y, int color) {
        for (int i = 0; i < wrappedMessage.size(); i++) {
            FormattedCharSequence line = wrappedMessage.get(i);
            drawText(guiGraphics, line, x, y + i * 9, color);
        }
    }
}
