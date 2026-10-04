package net.bivrik.fancynotify.notification;

public class NotificationStateMachine {
    private final Listener listener;

    private float elapsedTicks;
    private float timingTicks = 0;
    private NotificationState state = NotificationState.HIDDEN;

    public NotificationStateMachine(Listener listener) {
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

    public void forceHide() {
        changeState(NotificationState.HIDING, elapsedTicks);
    }

    public void update(float elapsedTicks, float offsetTicks, float durationTicks, float animationDurationTicks) {
        this.elapsedTicks = elapsedTicks;

        float offsetElapsedTicks = elapsedTicks - offsetTicks;

        switch (state) {
            case HIDDEN -> changeState(NotificationState.SHOWING, elapsedTicks);
            case SHOWING -> {
                if (offsetElapsedTicks > animationDurationTicks) {
                    changeState(NotificationState.VISIBLE, elapsedTicks);
                }
            }
            case VISIBLE -> {
                if (offsetElapsedTicks >= durationTicks - animationDurationTicks) {
                    changeState(NotificationState.HIDING, elapsedTicks);
                }
            }
            case HIDING -> {
                if (offsetElapsedTicks >= durationTicks) {
                    changeState(NotificationState.REMOVAL, elapsedTicks);
                }
            }
        }
    }

    private void changeState(NotificationState state, float timingTicks) {
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
