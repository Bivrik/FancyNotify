package net.bivrik.fancynotify.impl;

import com.evandev.fieldguide.Constants;
import com.evandev.fieldguide.api.GuideEntry;
import com.evandev.fieldguide.api.variant.VariantDef;
import com.evandev.fieldguide.api.variant.VariantProvider;
import com.evandev.fieldguide.client.ClientFieldGuideManager;
import com.evandev.fieldguide.client.gui.toasts.FieldGuideToast;
import com.evandev.fieldguide.client.gui.util.EntryRenderHelper;
import com.evandev.fieldguide.config.ClientConfig;
import com.evandev.fieldguide.entry.EntryResolver;
import com.evandev.fieldguide.util.DummyEntities;
import com.evandev.fieldguide.variant.FieldGuideVariantManager;
import net.bivrik.compat.fieldguide.FieldGuideIconRenderer;
import net.bivrik.compat.fieldguide.FieldGuideToastAccessor;
import net.bivrik.compat.fieldguide.notification.FieldGuideNotification;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.platform.services.IFieldGuideApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Objects;

public class FieldGuideImpl implements IFieldGuideApi {
    @Override
    public boolean tryHandleToast(Toast toast, NotificationManager notificationManager) {
        if (!(toast instanceof FieldGuideToast)) return false;

        FieldGuideToastAccessor accessor = (FieldGuideToastAccessor) toast;
        String variantId = accessor.getVariantId();
        Object entry = accessor.getEntry();
        Component title = ClientFieldGuideManager.getEntryName(accessor.getEntry());
        int titleColor = ClientConfig.get().getTextTitleColorInt();

        Object coreEntry = EntryResolver.resolveCoreEntry(entry);
        GuideEntry guideEntry = entry instanceof GuideEntry ? (GuideEntry) entry : null;

        boolean isVirtual = guideEntry != null && guideEntry.isVirtual() && guideEntry.virtualData() != null;
        boolean isTutorial = isVirtual && "tutorial".equals(guideEntry.virtualData().virtualType());

        Entity cachedEntity;
        if (coreEntry instanceof EntityType<?> type) {
            cachedEntity = DummyEntities.create(type, Objects.requireNonNull(Minecraft.getInstance().level), EntitySpawnReason.TRIGGERED);

            if (variantId != null && cachedEntity instanceof Mob mob) {
                VariantProvider<Mob> provider = FieldGuideVariantManager.getProvider(mob);
                if (provider != null) {
                    List<VariantDef> variants = FieldGuideVariantManager.getVariants(mob);
                    for (VariantDef variant : variants) {
                        if (variant.id().equals(variantId)) {
                            provider.apply(mob, variant);
                            break;
                        }
                    }
                }
            }
        } else {
            cachedEntity = null;
        }

        FieldGuideIconRenderer iconRenderer = null;
        if (entry instanceof GuideEntry && guideEntry.isStructure() && coreEntry instanceof Block) {
            iconRenderer = (graphics, x, y) -> EntryRenderHelper.renderStructure(graphics, guideEntry, x, y, 24, true, false, 1.0f);
        } else if (isTutorial) {
            iconRenderer = (graphics, x, y) -> EntryRenderHelper.renderTutorial(graphics, (GuideEntry) entry, x, y, 24, 24, true, false, 1.0f);
        } else if (coreEntry instanceof EntityType<?> && cachedEntity != null) {
            iconRenderer = (graphics, x, y) -> EntryRenderHelper.renderEntityNormalized(graphics, cachedEntity, x, y, 24, 24, true, false, 1.0f, false);
        } else if (coreEntry instanceof Block block) {
            iconRenderer = (graphics, x, y) -> EntryRenderHelper.renderBlock(graphics, block, x, y, 12.0f, true, false, 1.0f);
        } else if (coreEntry instanceof Item item) {
            iconRenderer = (graphics, x, y) -> EntryRenderHelper.renderItem(graphics, item, x, y, 20.0f, true, false, 1.0f);
        }

        if (iconRenderer == null) {
            iconRenderer = (graphics, x, y) -> graphics.blit(Constants.TOAST_ICON, 8, 8, 0, 0, 16, 16, 16, 16);
        }

        notificationManager.add(new FieldGuideNotification(notificationManager, title, titleColor, iconRenderer));
        return true;
    }
}
