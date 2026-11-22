package net.bivrik.fancynotify.api.gui.notification;

import org.jetbrains.annotations.ApiStatus;

/**
 * Provided by the engine on every update. Not meant to be implemented by API users
 */
@ApiStatus.NonExtendable
@ApiStatus.Experimental
public interface NotificationContext {

    float getGlobalX();

    float getGlobalY();

    float getElapsedTicks();

    int getAnimationDurationTicks();
}
