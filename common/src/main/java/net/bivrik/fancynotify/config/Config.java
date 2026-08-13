package net.bivrik.fancynotify.config;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Config {
    private transient final String path;

    protected Config(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    protected ToStringBuilder getBaseStringBuilder() {
        return new ToStringBuilder(this).append("path", path);
    }

    @Override
    public String toString() {
        return getBaseStringBuilder().toString();
    }
}
