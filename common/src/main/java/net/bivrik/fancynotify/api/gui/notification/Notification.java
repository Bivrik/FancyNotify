package net.bivrik.fancynotify.api.gui.notification;

import net.bivrik.fancynotify.api.gui.icon.Icon;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

public abstract class Notification {
    protected final Minecraft minecraft = Minecraft.getInstance();

    private List<FormattedCharSequence> messageLines;

    private Component title = Component.empty();
    private Component message = Component.empty();
    private Icon icon;

    private int titleWidth = -1;
    private int userPreferredWidth;

    protected Notification(Component title, Component message) {
        setDisplay(title, message);
    }

    protected final void setDisplay(Component title, Component message) {
        this.title = title == null ? Component.empty() : title;
        this.message = message == null ? Component.empty() : message;

        clearDisplayCache();
    }

    protected final void setIcon(Icon icon) {
        this.icon = icon;
    }

    @ApiStatus.Internal
    public final void setUserPreferredWidth(int userPreferredWidth) {
        this.userPreferredWidth = userPreferredWidth;

        clearDisplayCache();
    }

    private List<FormattedCharSequence> splitIntoLines(Component text) {
        return List.copyOf(minecraft.font.split(text, getTextMaxWidth()));
    }

    protected int getTextMaxWidth() {
        return Math.max(0, getWidth() - getTextLeftPadding() - getTextRightPadding());
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
        if (messageLines == null) {
            messageLines = splitIntoLines(message);
        }

        return messageLines;
    }

    protected final void clearDisplayCache() {
        messageLines = null;

        titleWidth = -1;
    }

    protected final int getCenterX() {
        return getWidth() / 2;
    }

    protected final int getCenterY() {
        return getHeight() / 2;
    }

    public final int getWidth() {
        return Math.max(getMinWidth(), userPreferredWidth);
    }

    protected int getMinWidth() {
        return getTitleWidth() + getTextLeftPadding() + getTextRightPadding();
    }

    protected final int getTitleWidth() {
        if (titleWidth == -1) {
            titleWidth = minecraft.font.width(title);
        }

        return titleWidth;
    }

    public int getHeight() {
        return getBaseHeight() + getMessageLines().size() * minecraft.font.lineHeight;
    }

    protected int getBaseHeight() {
        return 23;
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

    @ApiStatus.Internal
    public final void update(NotificationContext context) {
        onUpdate(context);
    }

    @ApiStatus.Internal
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
