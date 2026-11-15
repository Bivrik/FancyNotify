package net.bivrik.fancynotify.api.gui.notification;

import java.util.Objects;

public final class NotificationIdentifier {
    public static final NotificationIdentifier NONE = of("fancynotify", "none");

    private final String namespace;
    private final String id;

    private NotificationIdentifier(String namespace, String id) {
        this.namespace = Objects.requireNonNull(namespace);
        this.id = Objects.requireNonNull(id);
    }

    public static NotificationIdentifier of(String namespace, String id) {
        return new NotificationIdentifier(namespace, id);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NotificationIdentifier other)) {
            return false;
        }

        return namespace.equals(other.namespace) && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, id);
    }

    @Override
    public String toString() {
        return namespace + ":" + id;
    }
}
