package net.bivrik.fancynotify.eventbus;

import net.bivrik.fancynotify.core.Log;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class EventBus implements IEventBus {
    private final static int MAX = 50;

    private final ConcurrentHashMap<Class<? extends Event>, List<EventSubscriber>> eventSubscribers = new ConcurrentHashMap<>();

    private int temp = 0;

    @Override
    public void register(final Object listener) {
        temp++;
        Class<?> listenerClass = listener.getClass();
        for (Method method : listenerClass.getMethods()) {
            if (method.isAnnotationPresent(SubscribeEvent.class)) {
                Class<? extends Event> eventType = getEventType(method);
                EventSubscriber subscriber = new EventSubscriber(listener, method, eventType);
                eventSubscribers.computeIfAbsent(eventType, v -> new CopyOnWriteArrayList<>()).add(subscriber);
                //Log.info("Registered method " + method.getName() + " (" + listener.getClass().getSimpleName() + ") for " + eventType.getSimpleName() + " (" + eventSubscribers.get(eventType).size() + ")");
            }
        }
        if (temp >= MAX) {
            temp = 0;
            for (var entry : eventSubscribers.entrySet()) {
                List<EventSubscriber> subscribers = entry.getValue();
                subscribers.removeIf(subscriber -> subscriber.getTarget() == null);
                if (subscribers.isEmpty()) {
                    eventSubscribers.remove(entry.getKey(), subscribers);
                }
            }
        }
    }

    private Class<? extends Event> getEventType(final Method method) {
        Class<?>[] paramTypes = method.getParameterTypes();
        if (paramTypes.length != 1) {
            throw new IllegalArgumentException("Subscriber method " + method + " must have only 1 argument (event)");
        }
        Class<?> paramType = paramTypes[0];
        if (!Event.class.isAssignableFrom(paramType)) {
            throw new IllegalArgumentException("Subscriber method " + method + " takes an argument that is not an event");
        }
        @SuppressWarnings("unchecked")
        Class<? extends Event> eventType = (Class<? extends Event>) paramType;
        return eventType;
    }

    @Override
    public void unregister(final Object listener) {
        for (var entry : eventSubscribers.entrySet()) {
            List<EventSubscriber> subscribers = entry.getValue();
            subscribers.removeIf(subscriber -> subscriber.getTarget() == listener);
            //Log.info("Unregistered (" + listener.getClass().getSimpleName() + ") from " + entry.getKey() + " (" + subscribers.size() + ")");
            if (subscribers.isEmpty()) {
                eventSubscribers.remove(entry.getKey(), subscribers);
            }
        }
    }

    @Override
    public <T extends Event> void send(final T event) {
        List<EventSubscriber> subscribers = eventSubscribers.get(event.getClass());
        if (subscribers == null) {
            return;
        }

        //printDebugInfo();
        //int before = subscribers.size();

        subscribers.removeIf(subscriber -> !subscriber.invoke(event));
        if (subscribers.isEmpty()) {
            eventSubscribers.remove(event.getClass());
        }

        //Log.info("Sent " + event.getClass().getSimpleName() + " (from " + before + " to " + subscribers.size() + ")");
    }

    @Override
    public void printDebugInfo() {
        Log.info("==================");
        Log.info("Event Subscribers");
        Log.info("==================");
        for (var entry : eventSubscribers.entrySet()) {
            Log.info(entry.getKey().getSimpleName());
            StringBuilder eventSubscribers = new StringBuilder("-");
            for (var listener : entry.getValue()) {
                var l = listener.getTarget();
                eventSubscribers.append(" ").append(l == null ? "null" : l.toString());
            }
            Log.info(eventSubscribers.toString());
        }
        Log.info("==================");
    }
}
