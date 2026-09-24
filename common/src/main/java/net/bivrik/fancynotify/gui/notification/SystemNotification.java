package net.bivrik.fancynotify.gui.notification;

import net.bivrik.fancynotify.FancyNotify;
import net.bivrik.fancynotify.api.Notification;
import net.bivrik.fancynotify.api.NotificationGraphics;
import net.bivrik.fancynotify.core.Log;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class SystemNotification extends FancyExpandableNotification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/system");

    private final Identifier id;

    public SystemNotification(Identifier id, Component title, Component description) {
        super(title, description);

        this.id = id;
    }

    @Override
    public boolean shouldDisplay() {
        return this.filters.isSystemNotificationEnabled.get();
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public int getLifeTimeTicks() {
        return id.lifeTimeTicks();
    }

    @Override
    public void expand(Notification expansion) {
        SystemNotification other = (SystemNotification) expansion;
        
        setDisplay(other.getTitle(), other.getMessage());
    }

    @Override
    public void draw(NotificationGraphics graphics, float partialTick) {
        graphics.sprite(BACKGROUND, 0, 0, getWidth(), getHeight());
        List<FormattedCharSequence> messageLines = getWrappedMessage();
        int alignment = Math.min(messageLines.size(), 1);
        graphics.text(getTitle(), getTextOffset(), 8 - alignment, Color.yellow.getRGB());
        graphics.multiline(messageLines, getTextOffset(), 18, -1);
        graphics.sprite(id.sprite(), 6, getCenterY() - 10, 20, 20);
    }

    public enum Identifier {
        NARRATOR(ResourceLocations.of("icons/narrator"), 80),
        WORLD_BACKUP(ResourceLocations.of("icons/storage")),
        PACK_LOAD_FAILURE(ResourceLocations.of("icons/folder")),
        WORLD_ACCESS_FAILURE(ResourceLocations.of("icons/storage")),
        PACK_COPY_FAILURE(ResourceLocations.of("icons/folder")),
        FILE_DROP_FAILURE(ResourceLocations.of("icons/storage")),
        PERIODIC_NOTIFICATION,
        LOW_DISK_SPACE(ResourceLocations.of("icons/storage"), 220),
        CHUNK_LOAD_FAILURE(ResourceLocations.of("icons/chunk")),
        CHUNK_SAVE_FAILURE(ResourceLocations.of("icons/chunk")),
        UNSECURE_SERVER_WARNING(220);

        private static final Map<SystemToast.SystemToastId, Identifier> VANILLA_ID_TO_NEW_ID = Map.ofEntries(
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

        private final ResourceLocation sprite;
        private final int lifeTimeTicks;

        Identifier(ResourceLocation sprite, int lifeTimeTicks) {
            this.sprite = sprite;
            this.lifeTimeTicks = lifeTimeTicks;
        }

        Identifier(int lifeTimeTicks) {
            this(ResourceLocations.of("icons/important"), lifeTimeTicks);
        }

        Identifier(ResourceLocation sprite) {
            this(sprite, 120);
        }

        Identifier() {
            this(ResourceLocations.of("icons/important"), 120);
        }

        public int lifeTimeTicks() {
            return lifeTimeTicks;
        }

        public ResourceLocation sprite() {
            return sprite;
        }

        public static Identifier fromSystemToastId(SystemToast.SystemToastId id) {
            Identifier systemToastId = VANILLA_ID_TO_NEW_ID.get(id);
            if (systemToastId == null) {
                Log.error("Failed to parse {}, falling back to PERIODIC_NOTIFICATION instead", id);
                return Identifier.PERIODIC_NOTIFICATION;
            }
            return systemToastId;
        }
    }
}
