package net.bivrik.fancynotify.config;

import net.bivrik.fancynotify.JsonHelper;
import net.bivrik.fancynotify.core.Constants;
import net.bivrik.fancynotify.core.Log;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ConfigManager {
    private static final org.slf4j.Logger LOGGER = Log.getSpecificLogger(ConfigManager.class);

    public static final String CONFIG_FOLDER_PATH = "./config/" + Constants.MOD_ID + "/";
    private static final File CONFIG_FOLDER = new File(CONFIG_FOLDER_PATH);

    private final Map<Class<? extends Config>, Config> configs = new HashMap<>();

    public ConfigManager() {
        load(new GeneralConfig());
        load(new FiltersConfig());
    }

    public GeneralConfig getGeneralConfig() {
        return (GeneralConfig) configs.get(GeneralConfig.class);
    }

    public FiltersConfig getFiltersConfig() {
        return (FiltersConfig) configs.get(FiltersConfig.class);
    }

    private <T extends Config> void load(T config) {
        configs.put(config.getClass(), read(config));
    }

    private <T extends Config> T read(T config) {
        File configFile = new File(config.getPath());
        @SuppressWarnings("unchecked")
        Class<T> configClass = (Class<T>) config.getClass();
        if (!configFile.exists()) {
            LOGGER.info("Creating new config {}", configClass.getSimpleName());
            write(configClass);
            return config;
        }
        Optional<T> optionalConfig = JsonHelper.tryToRead(configFile, configClass);
        if (optionalConfig.isEmpty()) {
            LOGGER.warn("Could not read config {} from {}", configClass.getSimpleName(), configFile.getPath());
            return config;
        }
        LOGGER.info("Successfully read config {}", configClass.getSimpleName());
        T result = optionalConfig.get();
        if (result instanceof IListenerRegistrar listenerRegistrar) {
            listenerRegistrar.registerListeners();
        }
        LOGGER.info(result.toString());
        return result;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public <T extends Config> void write(Class<T> configClass) {
        if (!CONFIG_FOLDER.exists()) {
            CONFIG_FOLDER.mkdir();
        }
        Config config = configs.getOrDefault(configClass, tryGetInstance(configClass));
        boolean isSuccessful = JsonHelper.tryToWrite(new File(config.getPath()), config);
        if (!isSuccessful) {
            LOGGER.error("Could not write config {} in {}", configClass.getSimpleName(), config.getPath());
        } else {
            LOGGER.info("Successfully wrote config {} in {}", configClass.getSimpleName(), config.getPath());
        }
    }

    private <T extends Config> T tryGetInstance(Class<T> configClass) {
        try {
            return configClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create an instance of " + configClass.getSimpleName() + ": " + e);
        }
    }
}
