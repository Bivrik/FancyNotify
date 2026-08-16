package net.bivrik.fancynotify;

import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.notification.gui.BiomeNotification;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class BiomeManager {
    // maybe check biome specific first
    // then check for tags
    // and in the end to default icon if nothing is found
    private static final HashMap<TagKey<Biome>, Item> TAGS_ICONS = new HashMap<>();

    static {
        TAGS_ICONS.put(BiomeTags.IS_FOREST, Items.OAK_SAPLING);
        TAGS_ICONS.put(BiomeTags.IS_BADLANDS, Items.TERRACOTTA);
        TAGS_ICONS.put(BiomeTags.IS_BEACH, Items.SAND);
        TAGS_ICONS.put(BiomeTags.IS_END, Items.END_STONE);
        TAGS_ICONS.put(BiomeTags.IS_HILL, Items.GRAVEL);
        TAGS_ICONS.put(BiomeTags.IS_JUNGLE, Items.JUNGLE_SAPLING);
        TAGS_ICONS.put(BiomeTags.IS_MOUNTAIN, Items.STONE);
        TAGS_ICONS.put(BiomeTags.IS_NETHER, Items.NETHERRACK);
        TAGS_ICONS.put(BiomeTags.IS_OCEAN, Items.KELP);
        TAGS_ICONS.put(BiomeTags.IS_RIVER, Items.WATER_BUCKET);
        TAGS_ICONS.put(BiomeTags.IS_SAVANNA, Items.ACACIA_SAPLING);
        TAGS_ICONS.put(BiomeTags.IS_TAIGA, Items.SPRUCE_SAPLING);
        TAGS_ICONS.put(BiomeTags.ALLOWS_SURFACE_SLIME_SPAWNS, Items.SLIME_BALL);
    }

    private static final Item DEFAULT_ICON = Items.GRASS_BLOCK;
    private static final int DELAY_TICKS = 40;

    private final Minecraft minecraft;
    private final NotificationManager notificationManager;

    private int counterTicks;
    private Biome currentBiome;

    public BiomeManager(Minecraft minecraft, NotificationManager notificationManager) {
        this.minecraft = minecraft;
        this.notificationManager = notificationManager;
    }

    public void tick() {
        counterTicks++;
        if (counterTicks >= DELAY_TICKS) {
            counterTicks = 0;

            ClientLevel level = minecraft.level;
            Entity camera = minecraft.getCameraEntity();
            if (level == null || camera == null) {
                return;
            }

            Holder<Biome> biomeHolder = level.getBiome(camera.blockPosition());
            if (currentBiome == biomeHolder.value()) {
                return;
            }

            currentBiome = biomeHolder.value();
            Identifier biomeId = biomeHolder.unwrap().map(ResourceKey::identifier, null);
            Component biomeName = getBiomeComponent(biomeId, currentBiome);
            Item icon = DEFAULT_ICON;
            for (var entry : TAGS_ICONS.entrySet()) {
                if (biomeHolder.is(entry.getKey())) {
                    icon = entry.getValue();
                    break;
                }
            }
            notificationManager.add(new BiomeNotification(notificationManager, biomeName, icon.getDefaultInstance()));
        }
    }

    private Component getBiomeComponent(Identifier id, Biome biome) {
        if (id == null) {
            return Component.translatable("fancynotify.gui.biome.unknown", Component.literal(biome.toString()));
        }
        return locationToTitle(id);
    }

    private @NotNull Component locationToTitle(Identifier location) {
        String title = location.getPath();
        StringBuilder output = new StringBuilder(title.length());
        boolean isNextCharCapitalized = true;
        for (int i = 0; i < title.length(); i++) {
            char c = title.charAt(i);
            boolean isUnderscore = c == '_';
            output.append(isNextCharCapitalized ? Character.toTitleCase(c) : isUnderscore ? ' ' : c);
            isNextCharCapitalized = isUnderscore;
        }

        return Component.literal(output.toString());
    }


}
