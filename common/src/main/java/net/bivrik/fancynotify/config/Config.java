package net.bivrik.fancynotify.config;

public class Config {
    private transient final String path;

    protected Config(String path) {
        this.path = path;
    }

    // Probably temp, I need to come up with better solution for these shenanigans
    public void registerListeners() {}

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + String.format("{path='%s'}", path);
    }
}
