package net.bivrik.fancynotify.gui.notification;

import net.bivrik.fancynotify.api.gui.icon.SpriteIcon;
import net.bivrik.fancynotify.api.gui.notification.Notification;
import net.bivrik.fancynotify.api.gui.notification.NotificationGraphics;
import net.bivrik.fancynotify.api.gui.notification.NotificationIdentifier;
import net.bivrik.fancynotify.core.Log;
import net.bivrik.fancynotify.utility.Color;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;
import java.util.Map;

public class SystemNotification extends FancyExpandableNotification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/system");
    private static final int TITLE_COLOR = Color.YELLOW;

    private final Type type;

    public SystemNotification(Type type, Component title, Component description) {
        super(title, description);
        setIcon(new SpriteIcon(type.getSprite(), 20, 20));

        this.type = type;
    }

    @Override
    public boolean shouldDisplay() {
        return this.filters.isSystemNotificationEnabled.get();
    }

    @Override
    public NotificationIdentifier getId() {
        return type.getId();
    }

    @Override
    public int getDurationTicks() {
        return type.getDurationTicks();
    }

    @Override
    public void expand(Notification expansion) {
        SystemNotification other = (SystemNotification) expansion;
        
        setDisplay(other.getTitle(), other.getMessage());
    }

    @Override
    public void render(NotificationGraphics graphics) {
        graphics.sprite(BACKGROUND, 0, 0, getWidth(), getHeight());
        List<FormattedCharSequence> messageLines = getWrappedMessage();
        int alignment = Math.min(messageLines.size(), 1);
        graphics.text(getTitle(), getTextOffset(), 8 - alignment, TITLE_COLOR);
        graphics.multilineText(messageLines, getTextOffset(), 18, -1);
        graphics.icon(getIcon(), 6, getCenterY() - 10);
    }

    public enum Type {
        NARRATOR("narrator", "narrator", 80),
        WORLD_BACKUP("world_backup", "storage"),
        PACK_LOAD_FAILURE("pack_load_failure", "folder"),
        WORLD_ACCESS_FAILURE("world_access_failure", "storage"),
        PACK_COPY_FAILURE("pack_copy_failure", "folder"),
        FILE_DROP_FAILURE("file_drop_failure", "storage"),
        PERIODIC_NOTIFICATION("periodic_notification"),
        LOW_DISK_SPACE("low_disk_space", "storage", 220),
        CHUNK_LOAD_FAILURE("chunk_load_failure", "chunk"),
        CHUNK_SAVE_FAILURE("chunk_save_failure", "chunk"),
        UNSECURE_SERVER_WARNING("unsecure_server_warning", 220);

        private static final Map<SystemToast.SystemToastId, Type> VANILLA_ID_TO_TYPE = Map.ofEntries(
                Map.entry(SystemToast.SystemToastId.NARRATOR_TOGGLE, NARRATOR),
                Map.entry(SystemToast.SystemToastId.WORLD_BACKUP, WORLD_BACKUP),
                Map.entry(SystemToast.SystemToastId.PACK_LOAD_FAILURE, PACK_LOAD_FAILURE),
                Map.entry(SystemToast.SystemToastId.WORLD_ACCESS_FAILURE, WORLD_ACCESS_FAILURE),
                Map.entry(SystemToast.SystemToastId.PACK_COPY_FAILURE, PACK_COPY_FAILURE),
                Map.entry(SystemToast.SystemToastId.FILE_DROP_FAILURE, FILE_DROP_FAILURE),
                Map.entry(SystemToast.SystemToastId.PERIODIC_NOTIFICATION, PERIODIC_NOTIFICATION),
                Map.entry(SystemToast.SystemToastId.LOW_DISK_SPACE, LOW_DISK_SPACE),
                Map.entry(SystemToast.SystemToastId.CHUNK_LOAD_FAILURE, CHUNK_LOAD_FAILURE),
                Map.entry(SystemToast.SystemToastId.CHUNK_SAVE_FAILURE, CHUNK_SAVE_FAILURE),
                Map.entry(SystemToast.SystemToastId.UNSECURE_SERVER_WARNING, UNSECURE_SERVER_WARNING)
        );

        private final NotificationIdentifier id;
        private final ResourceLocation sprite;
        private final int durationTicks;

        Type(String id, String sprite, int durationTicks) {
            this.id = NotificationIdentifier.of("minecraft", "system/" + id);
            this.sprite = ResourceLocation.fromNamespaceAndPath("fancynotify", "icons/" + sprite);
            this.durationTicks = durationTicks;
        }

        Type(String id, String sprite) {
            this(id, sprite, 120);
        }

        Type(String id, int durationTicks) {
            this(id, "important", durationTicks);
        }

        Type(String id) {
            this(id, "important", 120);
        }

        public NotificationIdentifier getId() {
            return id;
        }

        public ResourceLocation getSprite() {
            return sprite;
        }

        public int getDurationTicks() {
            return durationTicks;
        }

        public static Type fromSystemToastId(SystemToast.SystemToastId id) {
            Type type = VANILLA_ID_TO_TYPE.get(id);
            if (type == null) {
                Log.error("Failed to parse {}, falling back to PERIODIC_NOTIFICATION instead", id);
                return Type.PERIODIC_NOTIFICATION;
            }
            return type;
        }
    }
}
