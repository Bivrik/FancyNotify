package net.bivrik.fancynotify.gui;

import net.bivrik.fancynotify.NotificationManager;
import net.bivrik.fancynotify.ResourceLocations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.Map;

public class SystemNotification extends ExpandableNotification {
    private static final ResourceLocation BACKGROUND = ResourceLocations.of("notifications/system");

    private Identifier id;
    private int backgroundOffset;

    public SystemNotification(NotificationManager manager, Identifier id, Component title, Component description) {
        super(manager);

        setValues(id, title, description);
    }

    private void setValues(Identifier id, Component title, Component description) {
        this.id = id;
        this.setDisplay(title, description);
        this.backgroundOffset = (this.messageLines.size() - 1) * 9;
    }

    @Override
    public boolean shouldDisplay() {
        return this.filtersConfig.isSystemNotificationEnabled.get();
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public int getHeight() {
        return super.getHeight() + backgroundOffset;
    }

    @Override
    protected void expand(ExpandableNotification notification) {
        if (notification instanceof SystemNotification systemNotification) {
            setValues(systemNotification.id, systemNotification.title, systemNotification.message);
        }
    }

    @Override
    public void draw(GuiGraphics guiGraphics) {
        drawSprite(guiGraphics, BACKGROUND, 0, 0, this.getWidth(), this.getHeight());
        int alignment = Math.min(this.messageLines.size(), 1);
        drawText(guiGraphics, title, 29, 8 - alignment, Color.yellow.getRGB());
        for (int i = 0; i < this.messageLines.size(); i++) {
            var line = this.messageLines.get(i);
            drawText(guiGraphics, line, 29, 18 + i * 9, -1);
        }
        drawSprite(guiGraphics, id.sprite, 6, this.getCenterY() - 10, 20, 20);
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

        private static final Map<SystemToast.SystemToastId, Identifier> SYSTEM_TOAST_TO_ID = Map.ofEntries(
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

        public int getLifeTimeTicks() {
            return lifeTimeTicks;
        }

        public ResourceLocation getSprite() {
            return sprite;
        }

        public static Identifier fromSystemToastId(SystemToast.SystemToastId id) {
            return SYSTEM_TOAST_TO_ID.get(id);
        }
    }
}
