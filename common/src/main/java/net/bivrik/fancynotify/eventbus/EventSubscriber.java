package net.bivrik.fancynotify.eventbus;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class EventSubscriber {
    private final WeakReference<Object> targetReference;
    private final Method method;
    private final Class<? extends Event> eventType;

    public EventSubscriber(Object target, Method method, Class<? extends Event> eventType) {
        this.targetReference = new WeakReference<>(target);
        this.method = method;
        this.eventType = eventType;
        this.method.setAccessible(true);
    }

    public Object getTarget() {
        return targetReference.get();
    }

    public boolean invoke(Event event) {
        Object target = targetReference.get();
        if (target == null) {
            return false;
        }

        if (!eventType.isAssignableFrom(event.getClass())) {
            throw new IllegalArgumentException("Expected " + eventType.getName() + " but got instead " + event.getClass().getName());
        }

        try {
            method.invoke(target, event);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot access subscriber method: " + e.getCause());
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Error in subscriber method: " + e.getCause());
        }

        return true;
    }
}
