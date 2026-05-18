package net.bivrik.fancynotify.config;

import net.bivrik.fancynotify.JsonHelper;
import net.bivrik.fancynotify.core.Constants;
import net.bivrik.fancynotify.core.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ConfigManager {
    private static final org.slf4j.Logger LOGGER = Logger.getSpecificLogger(ConfigManager.class);
    private static final String CONFIG_FOLDER_PATH = "./config/" + Constants.MOD_ID + "/";
    private static final String GENERAL_CONFIG_PATH = CONFIG_FOLDER_PATH + "general.json";
    private static final String FILTERS_CONFIG_PATH = CONFIG_FOLDER_PATH + "filters.json";

    private static final File CONFIG_FOLDER = new File(CONFIG_FOLDER_PATH);

    private final Map<Class<? extends Config>, Config> configs = new HashMap<>();

    public ConfigManager() {
        load(new GeneralConfig(GENERAL_CONFIG_PATH));
        load(new FiltersConfig(FILTERS_CONFIG_PATH));
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

    @SuppressWarnings("unchecked")
    private <T extends Config> T read(T config) {
        File configFile = new File(config.getPath());
        Class<T> configClass = (Class<T>) config.getClass();
        if (!configFile.exists()) {
            LOGGER.info("Creating new config {}", configClass.getSimpleName());
            write(config);
            return config;
        }
        Optional<T> optionalConfig = JsonHelper.tryToRead(configFile, configClass);
        if (optionalConfig.isEmpty()) {
            LOGGER.warn("Could not read config {} from {}", configClass.getSimpleName(), configFile.getPath());
            return config;
        }
        LOGGER.info("Successfully read config {} from {}", configClass.getSimpleName(), configFile.getPath());
        return optionalConfig.get();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public <T extends Config> void write(T reference) {
        if (!CONFIG_FOLDER.exists()) {
            CONFIG_FOLDER.mkdir();
        }
        Config config = configs.get(reference.getClass());
        if (config == null) {
            config = reference;
        }
        boolean isSuccessful = JsonHelper.tryToWrite(new File(config.getPath()), config);
        if (!isSuccessful) {
            LOGGER.error("Could not write config {} in {}", reference.getClass().getSimpleName(), config.getPath());
        } else {
            LOGGER.info("Successfully wrote config {} in {}", reference.getClass().getSimpleName(), config.getPath());
        }
    }
}
