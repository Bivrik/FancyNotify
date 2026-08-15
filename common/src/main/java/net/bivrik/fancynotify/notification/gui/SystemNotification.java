package net.bivrik.fancynotify.notification.gui;

import net.bivrik.fancynotify.notification.ExpandableNotification;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.Map;

public class SystemNotification extends ExpandableNotification {
    private final Identifier id;

    public SystemNotification(NotificationManager manager, Identifier id, Component title, Component description) {
        super(manager, title, description);

        this.id = id;
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
    public void draw(GuiGraphics guiGraphics) {
        drawBackground(guiGraphics, 0, 192);
        int alignment = Math.min(getWrappedMessage().size(), 1);
        drawText(guiGraphics, getTitle(), getTextOffset(), 8 - alignment, Color.yellow.getRGB());
        drawMessage(guiGraphics, getTextOffset(), 18, -1);
        drawTexture(guiGraphics, id.getSprite(), 6, getCenterY() - 10, 20, 20, 20, 20);
    }

    public enum Identifier {
        NARRATOR(ResourceLocations.of("textures/gui/icons/narrator.png"), 80),
        WORLD_BACKUP(ResourceLocations.of("textures/gui/icons/storage.png")),
        PACK_LOAD_FAILURE(ResourceLocations.of("textures/gui/icons/folder.png")),
        WORLD_ACCESS_FAILURE(ResourceLocations.of("textures/gui/icons/storage.png")),
        PACK_COPY_FAILURE(ResourceLocations.of("textures/gui/icons/folder.png")),
        PERIODIC_NOTIFICATION,
        UNSECURE_SERVER_WARNING(220);

        private static final Map<SystemToast.SystemToastIds, Identifier> SYSTEM_TOAST_TO_ID = Map.ofEntries(
                Map.entry(SystemToast.SystemToastIds.NARRATOR_TOGGLE, NARRATOR),
                Map.entry(SystemToast.SystemToastIds.WORLD_BACKUP, WORLD_BACKUP),
                Map.entry(SystemToast.SystemToastIds.PACK_LOAD_FAILURE, PACK_LOAD_FAILURE),
                Map.entry(SystemToast.SystemToastIds.WORLD_ACCESS_FAILURE, WORLD_ACCESS_FAILURE),
                Map.entry(SystemToast.SystemToastIds.PACK_COPY_FAILURE, PACK_COPY_FAILURE),
                Map.entry(SystemToast.SystemToastIds.PERIODIC_NOTIFICATION, PERIODIC_NOTIFICATION),
                Map.entry(SystemToast.SystemToastIds.UNSECURE_SERVER_WARNING, UNSECURE_SERVER_WARNING)
        );

        private final ResourceLocation sprite;
        private final int lifeTimeTicks;

        Identifier(ResourceLocation sprite, int lifeTimeTicks) {
            this.sprite = sprite;
            this.lifeTimeTicks = lifeTimeTicks;
        }

        Identifier(int lifeTimeTicks) {
            this(ResourceLocations.of("textures/gui/icons/important.png"), lifeTimeTicks);
        }

        Identifier(ResourceLocation sprite) {
            this(sprite, 120);
        }

        Identifier() {
            this(ResourceLocations.of("textures/gui/icons/important.png"), 120);
        }

        public int getLifeTimeTicks() {
            return lifeTimeTicks;
        }

        public ResourceLocation getSprite() {
            return sprite;
        }

        public static Identifier fromSystemToastId(SystemToast.SystemToastIds id) {
            return SYSTEM_TOAST_TO_ID.get(id);
        }
    }
}
