package net.bivrik.fancynotify.platform;

public interface PlatformHelper {

    String getName();

    boolean isModLoaded(String modId);

    boolean isDevelopmentEnvironment();

    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "Development" : "Production";
    }
}
