package net.bivrik.fancynotify.platform;

import net.bivrik.fancynotify.core.Log;
import net.bivrik.fancynotify.platform.services.IPlatformHelper;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Supplier;

/**
 * Utility class for services. They are used to access specific mod loader data.
 */
public final class Services {
    private Services() {}

    private static final org.slf4j.Logger LOGGER = Log.getSpecificLogger(Services.class);

    // Must-have services. Platforms have to be loaded
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    // Optional services. Can have fallback implementation
    // e.g. public static final IPlatformHelper SOME_SERVICE = load(ISomeServiceHelper.class);

    // Fallbacks for optional services to avoid scenarios when one mod loader has a unique mod and others do not
    private static final Map<Class<?>, Supplier<?>> FALLBACKS = Map.of(
            // e.g. ISomeServiceHelper.class, SomeServiceHelper::new
    );

    // Loads a service that has implementation in every mod loader
    private static <T> T load(final Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz, Services.class.getClassLoader())
                .findFirst().orElseThrow(() -> new IllegalStateException("Failed to load service for: " + clazz.getName()));
        LOGGER.info("Loaded '{}' for service: {}", loadedService.getClass().getName(), clazz.getSimpleName());
        return loadedService;
    }

    // Loads a service that might be missing in one mod loader, but present in another one
    private static <T> T loadOptional(final Class<T> clazz) {
        try {
            return load(clazz);
        } catch (IllegalStateException e) {
            final Supplier<?> fallback = FALLBACKS.get(clazz);
            if (fallback == null) {
                throw new IllegalStateException("No implementation or fallback found for optional service: " + clazz.getName());
            }
            @SuppressWarnings("unchecked")
            final T loadedFallbackService = (T) fallback.get();
            LOGGER.info("Loaded fallback '{}' for service: {}", loadedFallbackService.getClass().getName(), clazz.getSimpleName());
            return loadedFallbackService;
        }
    }
}
