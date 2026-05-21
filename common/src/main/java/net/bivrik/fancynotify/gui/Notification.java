package net.bivrik.fancynotify.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bivrik.fancynotify.Easing;
import net.bivrik.fancynotify.Keyframe;
import net.bivrik.fancynotify.NotificationManager;
import net.bivrik.fancynotify.config.FiltersConfig;
import net.bivrik.fancynotify.config.GeneralConfig;
import net.bivrik.fancynotify.core.Common;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.List;

public abstract class Notification {
    private static final Logger LOGGER = net.bivrik.fancynotify.core.Logger.getSpecificLogger(Notification.class);
    private static final Object NO_ID = new Object();

    protected Component title;
    protected Component message;
    protected List<FormattedCharSequence> messageLines;

    protected final NotificationManager notificationManager;
    protected final FiltersConfig filtersConfig;
    protected final SoundManager soundManager;
    protected final Font font;
    protected final int animationDurationTicks;
    private final float maxAlpha;
    private final int minWidth;

    protected float timeTicks = 0;
    protected float offsetTicks = 0;
    private float hidingTimingTicks;
    private float showingTimingTicks;
    private int titleWidth;

    private float x = 0;
    private float y = 0;
    private float alpha = 0;

    private State visibility = State.HIDDEN;

    public Notification(NotificationManager manager) {
        this.notificationManager = manager;
        this.filtersConfig = manager.getConfigManager().getFiltersConfig();
        this.minWidth = manager.getConfigManager().getGeneralConfig().notificationsWidth.get();
        this.font = manager.getMinecraft().font;
        this.soundManager = manager.getMinecraft().getSoundManager();
        this.animationDurationTicks = manager.getAnimationDurationTicks();
        this.maxAlpha = manager.getConfigManager().getGeneralConfig().notificationsTransparency.get();
    }

    protected void setDisplay(Component title, @Nullable Component message) {
        this.title = title;
        this.message = message == null ? Component.empty() : message;
        this.messageLines = getWrappedText(message);
    }

    public int getWidth() {
        if (title != null && font != null) {
            titleWidth = font.width(title);
        }
        return Math.max(titleWidth + getTextOffset() + 7, minWidth);
    }

    protected int getTextOffset() {
        return 29;
    }

    public int getHeight() {
        return 32;
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
        return visibility == State.REMOVAL;
    }

    protected void onRemoval() {}

    protected List<FormattedCharSequence> getWrappedText(@Nullable Component text) {
        Component nullableText = text == null ? Component.empty() : text;
        return font.split(nullableText, getWidth() - 36);
    }

    protected int getCenterY() {
        return getHeight() / 2;
    }

    protected int getLifeTimeTicks() {
        return 140;
    }

    public void show() {
        if (visibility == State.SHOWING) return;

        setState(State.SHOWING);
        showingTimingTicks = timeTicks;
        soundManager.play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_IN, 1, 1));
    }

    public void hide() {
        if (visibility == State.HIDING) return;

        setState(State.HIDING);
        hidingTimingTicks = timeTicks;
        soundManager.play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_OUT, 1, 1));
    }

    public void showingAnimation() {
        LOGGER.info("Showing...");

        float endX = 0;
        float endAlpha = maxAlpha;

        float showingProgress = Keyframe.getProgress(timeTicks, 0, animationDurationTicks);
        if (Keyframe.isActive(showingProgress)) {
            x = Easing.SINE_OUT.lerp(getWidth() + NotificationManager.PADDING, endX, showingProgress);
            alpha = Easing.SINE_OUT.lerp(0, endAlpha, showingProgress);
        }

        if (timeTicks >= showingTimingTicks + animationDurationTicks) {
            setState(State.VISIBLE);
            x = endX;
            alpha = endAlpha;
        }
    }

    public void hidingAnimation() {
        LOGGER.info("Hiding...");

        float endX = getWidth() + NotificationManager.PADDING;
        float endAlpha = 0;

        float hidingProgress = Keyframe.getProgress(timeTicks, (int) hidingTimingTicks, animationDurationTicks);
        if (Keyframe.isActive(hidingProgress)) {
            x = Easing.SINE_IN.lerp(0, endX, hidingProgress);
            alpha = Easing.SINE_IN.lerp(maxAlpha, endAlpha, hidingProgress);
        }

        if (timeTicks >= hidingTimingTicks + animationDurationTicks) {
            setState(State.REMOVAL);
            x = endX;
            alpha = endAlpha;
            onRemoval();
        }
    }

    public void update(float deltaTicks) {
        timeTicks += deltaTicks;

        updateState();
        checkState();
    }

    private void updateState() {
        if (visibility == State.HIDDEN) {
            show();
        } else if (visibility == State.VISIBLE && timeTicks - offsetTicks >= getLifeTimeTicks() * notificationManager.getMinecraft().options.notificationDisplayTime().get()) {
            hide();
        }
    }

    protected void checkState() {
        if (visibility == State.SHOWING) {
            showingAnimation();
        } else if (visibility == State.HIDING) {
            hidingAnimation();
        }
    }

    private void setState(State visibility) {
        this.visibility = visibility;
        LOGGER.info("Set state: {}", visibility);
    }

    public void render(GuiGraphics guiGraphics) {
        if (visibility == State.HIDDEN || visibility == State.REMOVAL) {
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
            guiGraphics.drawString(font, text, x, y, color, false);
            return;
        }

        int iAlpha = (int) (alpha * 255);
        int alphaColor = (iAlpha << 24) | (color & 0x00FFFFFF);
        guiGraphics.drawString(font, text, x, y, alphaColor, false);
    }

    protected void drawText(GuiGraphics guiGraphics, Component text, int x, int y, int color) {
        drawText(guiGraphics, text.getVisualOrderText(), x, y, color);
    }

    private enum State {
        HIDDEN,
        SHOWING,
        VISIBLE,
        HIDING,
        REMOVAL
    }
}
