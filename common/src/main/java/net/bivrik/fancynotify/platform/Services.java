package net.bivrik.fancynotify.platform;

import net.bivrik.fancynotify.core.Log;
import net.bivrik.fancynotify.platform.api.FieldGuideApi;
import net.bivrik.fancynotify.platform.api.SpectrumApi;
import net.bivrik.fancynotify.platform.fallback.FieldGuideFallback;
import net.bivrik.fancynotify.platform.fallback.SpectrumFallback;

import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Supplier;

/**
 * Utility class for services. They are used to communicate between loader specific implementations and common module
 */
public final class Services {
    private Services() {}

    private static final ClassLoader CLASS_LOADER = Services.class.getClassLoader();
    private static final org.slf4j.Logger LOGGER = Log.getSpecificLogger(Services.class);

    // Must-have services. Platform has to be loaded
    public static final PlatformHelper PLATFORM = load(PlatformHelper.class);

    // Optional services. Can have fallback implementation
    public static final SpectrumApi SPECTRUM = loadOptional(SpectrumApi.class, SpectrumFallback::new);
    public static final FieldGuideApi FIELD_GUIDE = loadOptional(FieldGuideApi.class, FieldGuideFallback::new);

    // Loads a service that has implementation in every mod loader
    private static <T> T load(final Class<T> clazz) {
        final T service = ServiceLoader.load(clazz, CLASS_LOADER).findFirst().orElseThrow(() -> new IllegalStateException("Failed to load service " + clazz.getName()));
        LOGGER.info("Successfully loaded service {}", service.getClass().getSimpleName());
        return service;
    }

    // Loads a service that might be missing in one mod loader, but present in another one
    private static <T> T loadOptional(final Class<T> clazz, final Supplier<? extends T> fallback) {
        final Optional<T> optionalService = ServiceLoader.load(clazz, CLASS_LOADER).findFirst();
        if (optionalService.isPresent()) {
            final T service = optionalService.get();
            LOGGER.info("Successfully loaded optional service {}", service.getClass().getSimpleName());
            return service;
        }
        final T fallbackService = fallback.get();
        LOGGER.info("Loaded fallback for optional service {}", fallbackService.getClass().getSimpleName());
        return fallbackService;
    }

    public static void bootstrap() {}
}
