package net.bivrik.fancynotify.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bivrik.fancynotify.Easing;
import net.bivrik.fancynotify.Keyframe;
import net.bivrik.fancynotify.NotificationManager;
import net.bivrik.fancynotify.config.ConfigManager;
import net.bivrik.fancynotify.config.FiltersConfig;
import net.bivrik.fancynotify.config.GeneralConfig;
import net.bivrik.fancynotify.core.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class Notification {
    private static final org.slf4j.Logger LOGGER = Log.getSpecificLogger(Notification.class);
    private static final Object NO_ID = new Object();

    protected Component title;
    protected Component message;
    protected List<FormattedCharSequence> messageLines;
    private int titleWidth;

    protected final Minecraft minecraft;
    protected final FiltersConfig filtersConfig;
    protected final GeneralConfig generalConfig;

    protected float timeTicks = 0;
    protected float offsetTicks = 0;
    private float animationTimingTicks;

    private float x = 0;
    private float y = 0;
    private float alpha = 0;

    private Visibility state = Visibility.HIDDEN;

    public Notification(NotificationManager manager, @NotNull Component title, @Nullable Component message) {
        Minecraft minecraft = manager.getMinecraft();
        ConfigManager configManager = manager.getConfigManager();

        this.minecraft = minecraft;
        this.filtersConfig = configManager.getFiltersConfig();
        this.generalConfig = configManager.getGeneralConfig();

        setDisplay(title, message);
    }

    protected final void setDisplay(@NotNull Component title, @Nullable Component message) {
        Component notNullMessage = message == null ? Component.empty() : message;
        this.title = title;
        this.titleWidth = minecraft.font.width(title);
        this.message = notNullMessage;
        this.messageLines = getWrappedText(notNullMessage);
    }

    private List<FormattedCharSequence> getWrappedText(@NotNull Component text) {
        return minecraft.font.split(text, getWidth() - 36);
    }

    // Waittt width changes but text doesnt get wrapped properly yknow shi
    public final int getWidth() {
        return Math.max(titleWidth + getTextOffset() + 7, generalConfig.notificationsWidth.get());
    }

    public int getHeight() {
        return 32;
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
        return state == Visibility.REMOVAL;
    }

    protected void onRemoval() {}

    protected int getLifeTimeTicks() {
        return 140;
    }

    public int getAnimationDurationTicks() {
        return 15;
    }

    public void show() {
        if (state == Visibility.SHOWING) return;

        setVisibility(Visibility.SHOWING);
        animationTimingTicks = timeTicks;
        minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_IN, 1, 1));
    }

    public void hide() {
        if (state == Visibility.HIDING) return;

        setVisibility(Visibility.HIDING);
        animationTimingTicks = timeTicks;
        minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_OUT, 1, 1));
    }

    public void showingAnimation() {
        LOGGER.info("Showing...");

        float startX = getWidth() + NotificationManager.PADDING;
        float startAlpha = 0;

        float endX = 0;
        float endAlpha = 1;

        float showingProgress = Keyframe.getProgress(timeTicks, animationTimingTicks, getAnimationDurationTicks());
        if (Keyframe.isActive(showingProgress)) {
            x = Easing.SINE_OUT.lerp(startX, endX, showingProgress);
            setAlpha(Easing.SINE_OUT.lerp(startAlpha, endAlpha, showingProgress));
        }

        if (timeTicks >= animationTimingTicks + getAnimationDurationTicks()) {
            setVisibility(Visibility.VISIBLE);
            x = endX;
            setAlpha(endAlpha);
        }
    }

    public void hidingAnimation() {
        LOGGER.info("Hiding...");

        float startX = 0;
        float startAlpha = 1;

        float endX = getWidth() + NotificationManager.PADDING;
        float endAlpha = 0;

        float hidingProgress = Keyframe.getProgress(timeTicks, animationTimingTicks, getAnimationDurationTicks());
        if (Keyframe.isActive(hidingProgress)) {
            x = Easing.SINE_IN.lerp(startX, endX, hidingProgress);
            setAlpha(Easing.SINE_IN.lerp(startAlpha, endAlpha, hidingProgress));
        }

        if (timeTicks >= animationTimingTicks + getAnimationDurationTicks()) {
            setVisibility(Visibility.REMOVAL);
            x = endX;
            setAlpha(endAlpha);
            onRemoval();
        }
    }

    private void setAlpha(float alpha) {
        this.alpha = alpha * generalConfig.notificationsTransparency.get();
    }

    public void update(float deltaTicks) {
        timeTicks += deltaTicks;
        Log.info("{}", alpha);

        updateState();
        checkState();

        onUpdate();
    }

    protected void onUpdate() {}

    private void updateState() {
        if (state == Visibility.HIDDEN) {
            show();
        } else if (state == Visibility.VISIBLE && timeTicks - offsetTicks >= getLifeTimeTicks() * minecraft.options.notificationDisplayTime().get()) {
            hide();
        }
    }

    private void checkState() {
        if (state == Visibility.SHOWING) {
            showingAnimation();
        } else if (state == Visibility.HIDING) {
            hidingAnimation();
        }
    }

    private void setVisibility(Visibility state) {
        this.state = state;
        LOGGER.info("State: {}", state);
    }

    public final void render(GuiGraphics guiGraphics) {
        if (state == Visibility.HIDDEN || state == Visibility.REMOVAL) {
            return;
        }

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0);
        draw(guiGraphics);
        guiGraphics.pose().popPose();
    }

    protected abstract void draw(GuiGraphics guiGraphics);

    protected void drawSprite(GuiGraphics guiGraphics, ResourceLocation sprite, int x, int y, int width, int height) {
        if (alpha == 1) {
            guiGraphics.blitSprite(sprite, x, y, width, height);
            return;
        }

        RenderSystem.enableBlend();
        guiGraphics.setColor(1, 1, 1, alpha);
        guiGraphics.blitSprite(sprite, x, y, width, height);
        guiGraphics.setColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
    }

    protected void drawTexture(GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int width, int height, int textureWidth, int textureHeight, int uOffset, int vOffset) {
        if (alpha == 1) {
            guiGraphics.blit(texture, x, y, uOffset, vOffset, width, height, textureWidth, textureHeight);
            return;
        }

        RenderSystem.enableBlend();
        guiGraphics.setColor(1, 1, 1, alpha);
        guiGraphics.blit(texture, x, y, uOffset, vOffset, width, height, textureWidth, textureHeight);
        guiGraphics.setColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
    }

    protected void drawTexture(GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int width, int height, int textureWidth, int textureHeight) {
        drawTexture(guiGraphics, texture, x, y, width, height, textureWidth, textureHeight, 0, 0);
    }

    protected void drawText(GuiGraphics guiGraphics, FormattedCharSequence text, int x, int y, int color) {
        if (alpha == 1) {
            guiGraphics.drawString(minecraft.font, text, x, y, color, false);
            return;
        }

        RenderSystem.enableBlend();
        guiGraphics.setColor(1, 1, 1, alpha);
        guiGraphics.drawString(minecraft.font, text, x, y, color, false);
        guiGraphics.setColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
    }

    protected void drawText(GuiGraphics guiGraphics, Component text, int x, int y, int color) {
        drawText(guiGraphics, text.getVisualOrderText(), x, y, color);
    }

    private enum Visibility {
        HIDDEN,
        SHOWING,
        VISIBLE,
        HIDING,
        REMOVAL
    }
}
