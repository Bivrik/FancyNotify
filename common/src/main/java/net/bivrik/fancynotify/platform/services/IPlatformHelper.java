package net.bivrik.fancynotify.platform.services;

public interface IPlatformHelper {
    /**
     * Gets the display name of the current platform, where mod is initialized.
     * @return The display name of the current platform.
     */
    String getName();

    /**
     * Checks if a mod with the given id is loaded.
     * @param modId the mod id to check.
     * @return <code>true</code> if the mod is loaded, <code>false</code> otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game runs in a development environment.
     * @return <code>true</code> if in a development environment, <code>false</code> otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the display name of the environment type.
     * @return The display name of the environment type.
     */
    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }
}
