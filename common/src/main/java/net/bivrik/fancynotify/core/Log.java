package net.bivrik.fancynotify.core;

import org.slf4j.LoggerFactory;

/**
 * Utility class for easier logging. Wrapper of {@link org.slf4j.Logger}.
 */
public final class Log {
    private Log() {}
    // Standard logger with constant mod id that is used by default
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Constants.MOD_NAME);

    /**
     * Gets a {@link org.slf4j.Logger} instance for the specified class. Uses mod id as a root and adds simple class name.
     * @param clazz the calling class.
     * @return the logger named "<code>MOD_NAME</code>/<code>SimpleClassName</code>".
     * @see org.slf4j.Logger
     */
    public static org.slf4j.Logger getSpecificLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(Constants.MOD_NAME + "/" + clazz.getSimpleName());
    }

    /**
     * Logs a message at the INFO level.
     * @param msg message to log.
     * @see org.slf4j.Logger#info(String)
     */
    public static void info(String msg) {
        LOGGER.info(msg);
    }

    /**
     * Logs a message with arguments at the INFO level.
     * @param msg message to log.
     * @param args arguments to log.
     * @see org.slf4j.Logger#info(String, Object...)
     */
    public static void info(String msg, Object... args) {
        LOGGER.info(msg, args);
    }

    /**
     * Logs a message at the WARN level.
     * @param msg message to log.
     * @see org.slf4j.Logger#warn(String)
     */
    public static void warn(String msg) {
        LOGGER.warn(msg);
    }

    /**
     * Logs a message with arguments at the WARN level.
     * @param msg message to log.
     * @param args arguments to log.
     * @see org.slf4j.Logger#warn(String, Object...)
     */
    public static void warn(String msg, Object... args) {
        LOGGER.warn(msg, args);
    }

    /**
     * Logs a message at the ERROR level.
     * @param msg message to log.
     * @see org.slf4j.Logger#error(String)
     */
    public static void error(String msg) {
        LOGGER.error(msg);
    }

    /**
     * Logs a message with arguments at the ERROR level.
     * @param msg message to log.
     * @param args arguments to log.
     * @see org.slf4j.Logger#error(String, Object...)
     */
    public static void error(String msg, Object... args) {
        LOGGER.error(msg, args);
    }
}
