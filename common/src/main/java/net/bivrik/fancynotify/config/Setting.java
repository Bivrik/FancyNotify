package net.bivrik.fancynotify.config;

import java.util.function.Consumer;

public class Setting<T> {
    private T value;
    private Consumer<T> listener;

    public Setting(T value) {
        this.value = value;
    }

    public void setListener(Consumer<T> listener) {
        this.listener = listener;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
        if (listener != null) {
            listener.accept(value);
        }
    }
}
