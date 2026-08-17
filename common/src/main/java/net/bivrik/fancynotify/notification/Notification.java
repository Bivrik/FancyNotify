package net.bivrik.fancynotify.notification;

import net.bivrik.fancynotify.FancyNotify;
import net.bivrik.fancynotify.config.ConfigManager;
import net.bivrik.fancynotify.config.FiltersConfig;
import net.bivrik.fancynotify.config.GeneralConfig;
import net.bivrik.fancynotify.eventbus.SubscribeEvent;
import net.bivrik.fancynotify.eventbus.event.NotificationWidthChangedEvent;
import net.bivrik.fancynotify.particle.Particle2DEngine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2fStack;

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
        return minecraft.font.split(text, getWidth() - 7 - getTextOffset());
    }

    @SubscribeEvent
    public void onNotificationWidthChanged(NotificationWidthChangedEvent event) {
        width = event.getWidth();
        wrappedMessage = getWrappedText(message);
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

    public final void render(GuiGraphicsExtractor graphics) {
        if (stateMachine.isInState(NotificationState.HIDDEN) || stateMachine.isInState(NotificationState.REMOVAL)) {
            return;
        }

        float halfWidth = getWidth() / 2.0f;
        float halfHeight = getHeight() / 2.0f;
        Matrix3x2fStack stack = graphics.pose();
        stack.pushMatrix();
        stack.translate(halfWidth, halfHeight);
        stack.scale(animator.getScaleX(), animator.getScaleY());
        stack.rotate((float) Math.toRadians(animator.getRotation()));
        stack.translate(animator.getX() - halfWidth, animator.getY() - halfHeight);
        draw(graphics);
        stack.popMatrix();
    }

    protected abstract void draw(GuiGraphicsExtractor graphics);

    protected void drawSprite(GuiGraphicsExtractor graphics, Identifier sprite, int x, int y, int width, int height) {
        int color = getColorWithAlpha(0x00ffffff);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, x, y, width, height, color);
    }

    protected void drawTexture(GuiGraphicsExtractor graphics, Identifier texture, int x, int y, int width, int height, int textureWidth, int textureHeight, float uOffset, float vOffset, int uWidth, int vHeight) {
        int color = getColorWithAlpha(0x00ffffff);
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, uOffset, vOffset, width, height, uWidth, vHeight, textureWidth, textureHeight, color);
    }

    protected void drawTexture(GuiGraphicsExtractor graphics, Identifier texture, int x, int y, int width, int height, int textureWidth, int textureHeight, float uOffset, float vOffset) {
        drawTexture(graphics, texture, x, y, width, height, textureWidth, textureHeight, uOffset, vOffset, width, height);
    }

    protected void drawTexture(GuiGraphicsExtractor graphics, Identifier texture, int x, int y, int width, int height) {
        drawTexture(graphics, texture, x, y, width, height, width, height, 0, 0);
    }

    protected void drawText(GuiGraphicsExtractor graphics, FormattedCharSequence text, int x, int y, int color) {
        int colorWithAlpha = getColorWithAlpha(color);
        graphics.text(minecraft.font, text, x, y, colorWithAlpha, false);
    }

    protected void drawText(GuiGraphicsExtractor graphics, Component text, int x, int y, int color) {
        drawText(graphics, text.getVisualOrderText(), x, y, color);
    }

    protected void drawMessage(GuiGraphicsExtractor graphics, int x, int y, int color) {
        for (int i = 0; i < wrappedMessage.size(); i++) {
            FormattedCharSequence line = wrappedMessage.get(i);
            drawText(graphics, line, x, y + i * 9, color);
        }
    }

    private int getColorWithAlpha(int color) {
        int intAlpha = Math.round(animator.getAlpha() * 255);
        return intAlpha << 24 | (color & 0x00ffffff);
    }
}
