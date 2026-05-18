package net.bivrik.fancynotify.config;

public class Setting<T> {
    private T value;

    public Setting(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
