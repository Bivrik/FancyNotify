package net.bivrik.fancynotify.api;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;

public abstract class Notification {
    private static final Object NO_ID = new Object();

    protected final Minecraft minecraft = Minecraft.getInstance();

    private final List<FormattedCharSequence> wrappedMessage = new ArrayList<>();

    private Component title;
    private Component message;

    private int preferableWidth;
    private int minWidth;

    protected Notification(Component title, Component message) {
        setDisplay(title, message);
    }

    protected final void setDisplay(Component title, Component message) {
        this.title = title == null ? Component.empty() : title;
        this.message = message == null ? Component.empty() : message;

        minWidth = minecraft.font.width(this.title) + getTextOffset() + 7;
    }

    public final void setPreferableWidth(int preferableWidth) {
        this.preferableWidth = preferableWidth;

        wrappedMessage.clear();
        wrappedMessage.addAll(wrapText(message));
    }

    private List<FormattedCharSequence> wrapText(Component text) {
        return minecraft.font.split(text, getWidth() - getTextOffset() - 7);
    }

    protected final Component getTitle() {
        return title;
    }

    protected final Component getMessage() {
        return message;
    }

    protected final List<FormattedCharSequence> getWrappedMessage() {
        return List.copyOf(wrappedMessage);
    }

    protected final int getCenterX() {
        return getWidth() / 2;
    }

    protected final int getCenterY() {
        return getHeight() / 2;
    }

    protected int getTextOffset() {
        return 29;
    }

    public int getWidth() {
        return Math.max(minWidth, preferableWidth);
    }

    public int getHeight() {
        return 23 + wrappedMessage.size() * 9;
    }

    public Object getId() {
        return NO_ID;
    }

    public boolean canExpandFrom(Notification other) {
        return false;
    }

    public boolean shouldDisplay() {
        return true;
    }

    public int getLifeTimeTicks() {
        return 140;
    }

    public void update(NotificationContext context) {}

    public void onShowing() {}

    public void onVisible() {}

    public void onHiding() {}

    public void onRemoval() {}

    public abstract void draw(NotificationGraphics graphics, float partialTick);
}
