package net.bivrik.fancynotify.gui;

import net.bivrik.fancynotify.core.Log;
import net.minecraft.client.Minecraft;

public class NotificationStateMachine {
    private final Minecraft minecraft;
    private final Listener listener;

    private float timeTicks;
    private float timingTicks = 0;
    private NotificationState state = NotificationState.HIDDEN;

    public NotificationStateMachine(Minecraft minecraft, Listener listener) {
        this.minecraft = minecraft;
        this.listener = listener;
    }

    public boolean isInState(NotificationState state) {
        return this.state == state;
    }

    public NotificationState getState() {
        return state;
    }

    public float getTimingTicks() {
        return timingTicks;
    }

    public void show() {
        changeState(NotificationState.SHOWING, timeTicks);
    }

    public void hide() {
        changeState(NotificationState.HIDING, timeTicks);
    }

    public void update(float timeTicks, float offsetTimeTicks, float animationDurationTicks, float lifeTimeTicks) {
        this.timeTicks = timeTicks;

        switch (state) {
            case HIDDEN -> changeState(NotificationState.SHOWING, timeTicks);
            case SHOWING -> {
                if (timeTicks - timingTicks > animationDurationTicks) {
                    changeState(NotificationState.VISIBLE, timeTicks);
                }
            }
            case VISIBLE -> {
                if (timeTicks - offsetTimeTicks >= lifeTimeTicks * minecraft.options.notificationDisplayTime().get() - animationDurationTicks) {
                    changeState(NotificationState.HIDING, timeTicks);
                }
            }
            case HIDING -> {
                if (timeTicks - offsetTimeTicks >= lifeTimeTicks * minecraft.options.notificationDisplayTime().get()) {
                    changeState(NotificationState.REMOVAL, timeTicks);
                }
            }
            case REMOVAL -> {}
        }
    }

    private void changeState(NotificationState state, float timingTicks) {
        Log.info("[{}] New state: {}", timingTicks, state);

        this.state = state;
        this.timingTicks = timingTicks;

        switch (state) {
            case SHOWING -> listener.onShowing();
            case VISIBLE -> listener.onVisible();
            case HIDING -> listener.onHiding();
            case REMOVAL -> listener.onRemoval();
        }
    }

    public interface Listener {
        default void onShowing() {}
        default void onVisible() {}
        default void onHiding() {}
        default void onRemoval() {}
    }
}
