package net.bivrik.fancynotify.api.gui.notification;

import net.bivrik.fancynotify.api.gui.icon.Icon;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;

public abstract class Notification {
    protected final Minecraft minecraft = Minecraft.getInstance();

    private List<FormattedCharSequence> messageLines = List.of();

    private Component title;
    private Component message;
    private Icon icon;

    private int preferredWidth;
    private int minWidth;

    protected Notification(Component title, Component message) {
        setDisplay(title, message);
    }

    protected final void setDisplay(Component title, Component message) {
        this.title = title == null ? Component.empty() : title;
        this.message = message == null ? Component.empty() : message;

        minWidth = minecraft.font.width(this.title) + getTextLeftPadding() + getTextRightPadding();
    }

    protected final void setIcon(Icon icon) {
        this.icon = icon;
    }

    public final void setPreferredWidth(int preferredWidth) {
        this.preferredWidth = preferredWidth;

        messageLines = List.copyOf(splitIntoLines(message));
    }

    private List<FormattedCharSequence> splitIntoLines(Component text) {
        return minecraft.font.split(text, getTextMaxWidth());
    }

    protected int getTextMaxWidth() {
        return getWidth() - getTextLeftPadding() - getTextRightPadding();
    }

    protected int getTextLeftPadding() {
        return 29;
    }

    protected int getTextRightPadding() {
        return 7;
    }

    protected final Component getTitle() {
        return title;
    }

    protected final Component getMessage() {
        return message;
    }

    protected final Icon getIcon() {
        return icon;
    }

    protected final List<FormattedCharSequence> getMessageLines() {
        return messageLines;
    }

    protected final int getCenterX() {
        return getWidth() / 2;
    }

    protected final int getCenterY() {
        return getHeight() / 2;
    }

    public int getWidth() {
        return Math.max(minWidth, preferredWidth);
    }

    public int getHeight() {
        return 23 + messageLines.size() * 9;
    }

    public NotificationIdentifier getId() {
        return NotificationIdentifier.NONE;
    }

    public boolean shouldDisplay() {
        return true;
    }

    public int getDurationTicks() {
        return 140;
    }

    public final void update(NotificationContext context) {
        onUpdate(context);
    }

    public final void render(NotificationGraphics graphics) {
        onRender(graphics);
    }

    public void onShowing() {}

    public void onVisible() {}

    public void onHiding() {}

    public void onRemoval() {}

    protected void onUpdate(NotificationContext context) {}

    protected abstract void onRender(NotificationGraphics graphics);
}
