package net.bivrik.fancynotify.eventbus;

public interface EventBus {
    void register(final Object listener);

    void unregister(final Object listener);

    <T extends Event> void send(final T event);
}
