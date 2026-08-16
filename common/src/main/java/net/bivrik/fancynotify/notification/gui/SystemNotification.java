package net.bivrik.fancynotify.notification.gui;

import net.bivrik.fancynotify.notification.ExpandableNotification;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.utility.Identifiers;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.awt.*;
import java.util.Map;

public class SystemNotification extends ExpandableNotification {
    private static final Identifier BACKGROUND = Identifiers.of("notifications/system");

    private final Ids id;

    public SystemNotification(NotificationManager manager, Ids id, Component title, Component description) {
        super(manager, title, description);

        this.id = id;
    }

    @Override
    public boolean shouldDisplay() {
        return this.filtersConfig.isSystemNotificationEnabled.get();
    }

    @Override
    public Ids getId() {
        return id;
    }

    @Override
    protected int getLifeTimeTicks() {
        return id.getLifeTimeTicks();
    }

    @Override
    protected void expand(ExpandableNotification notification) {
        if (notification instanceof SystemNotification systemNotification) {
            setDisplay(systemNotification.getTitle(), systemNotification.getMessage());
        }
    }

    @Override
    public void draw(GuiGraphicsExtractor GuiGraphicsExtractor) {
        drawSprite(GuiGraphicsExtractor, BACKGROUND, 0, 0, getWidth(), getHeight());
        int alignment = Math.min(getWrappedMessage().size(), 1);
        drawText(GuiGraphicsExtractor, getTitle(), getTextOffset(), 8 - alignment, Color.yellow.getRGB());
        drawMessage(GuiGraphicsExtractor, getTextOffset(), 18, -1);
        drawSprite(GuiGraphicsExtractor, id.getSprite(), 6, getCenterY() - 10, 20, 20);
    }

    public enum Ids {
        NARRATOR(Identifiers.of("icons/narrator"), 80),
        WORLD_BACKUP(Identifiers.of("icons/storage")),
        PACK_LOAD_FAILURE(Identifiers.of("icons/folder")),
        WORLD_ACCESS_FAILURE(Identifiers.of("icons/storage")),
        PACK_COPY_FAILURE(Identifiers.of("icons/folder")),
        FILE_DROP_FAILURE(Identifiers.of("icons/storage")),
        PERIODIC_NOTIFICATION,
        LOW_DISK_SPACE(Identifiers.of("icons/storage"), 220),
        CHUNK_LOAD_FAILURE(Identifiers.of("icons/chunk")),
        CHUNK_SAVE_FAILURE(Identifiers.of("icons/chunk")),
        UNSECURE_SERVER_WARNING(220),
        FRIEND_SYSTEM_NOTIFICATION(Identifiers.of("icons/friends"));

        private static final Map<SystemToast.SystemToastId, Ids> SYSTEM_TOAST_TO_ID = Map.ofEntries(
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
                Map.entry(SystemToast.SystemToastId.UNSECURE_SERVER_WARNING, UNSECURE_SERVER_WARNING),
                Map.entry(SystemToast.SystemToastId.FRIEND_SYSTEM_NOTIFICATION, FRIEND_SYSTEM_NOTIFICATION)
        );

        private final Identifier sprite;
        private final int lifeTimeTicks;

        Ids(Identifier sprite, int lifeTimeTicks) {
            this.sprite = sprite;
            this.lifeTimeTicks = lifeTimeTicks;
        }

        Ids(int lifeTimeTicks) {
            this(Identifiers.of("icons/important"), lifeTimeTicks);
        }

        Ids(Identifier sprite) {
            this(sprite, 120);
        }

        Ids() {
            this(Identifiers.of("icons/important"), 120);
        }

        public int getLifeTimeTicks() {
            return lifeTimeTicks;
        }

        public Identifier getSprite() {
            return sprite;
        }

        public static Ids fromSystemToastId(SystemToast.SystemToastId id) {
            return SYSTEM_TOAST_TO_ID.get(id);
        }
    }
}
