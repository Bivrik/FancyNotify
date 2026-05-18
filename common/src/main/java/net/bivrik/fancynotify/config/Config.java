package net.bivrik.fancynotify.config;

public class Config {
    private transient final String path;

    protected Config(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + String.format("{path='%s'}", path);
    }
}
