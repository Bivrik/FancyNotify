package net.bivrik.fancynotify;

import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.notification.gui.BiomeNotification;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

// Should optimize even better in the future, but I don't think this will be that bad
public class BiomeManager {
    private static final Map<ResourceKey<Biome>, Item> SINGLE_BIOME_ICONS = new HashMap<>(12);
    private static final Map<TagKey<Biome>, Item> BIOMES_ICONS = new HashMap<>(16);

    private static final Item DEFAULT_ICON = Items.GRASS_BLOCK;
    private static final int DELAY_TICKS = 30;

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
        if (counterTicks > DELAY_TICKS) {
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

            Item icon = DEFAULT_ICON;
            for (TagKey<Biome> tagKey : biomeHolder.tags().toList()) {
                Item tagBiomeIcon = BIOMES_ICONS.get(tagKey);
                if (tagBiomeIcon != null) {
                    icon = tagBiomeIcon;
                    break;
                }
            }
            if (icon == DEFAULT_ICON) {
                ResourceKey<Biome> biomeKey = biomeHolder.unwrap().left().orElse(null);
                if (biomeKey != null) {
                    Item singleBiomeIcon = SINGLE_BIOME_ICONS.get(biomeKey);
                    if (singleBiomeIcon != null) {
                        icon = singleBiomeIcon;
                    }
                }
            }

            Identifier biomeId = biomeHolder.unwrap().map(ResourceKey::identifier, null);
            Component biomeName = getBiomeComponent(biomeId, currentBiome);
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

    static {
        SINGLE_BIOME_ICONS.put(Biomes.CHERRY_GROVE, Items.CHERRY_LEAVES);
        SINGLE_BIOME_ICONS.put(Biomes.MUSHROOM_FIELDS, Items.RED_MUSHROOM);
        SINGLE_BIOME_ICONS.put(Biomes.DRIPSTONE_CAVES, Items.POINTED_DRIPSTONE);
        SINGLE_BIOME_ICONS.put(Biomes.LUSH_CAVES, Items.SPORE_BLOSSOM);
        SINGLE_BIOME_ICONS.put(Biomes.DEEP_DARK, Items.SCULK_VEIN);
        SINGLE_BIOME_ICONS.put(Biomes.NETHER_WASTES, Items.NETHERRACK);
        SINGLE_BIOME_ICONS.put(Biomes.WARPED_FOREST, Items.WARPED_STEM);
        SINGLE_BIOME_ICONS.put(Biomes.CRIMSON_FOREST, Items.CRIMSON_STEM);
        SINGLE_BIOME_ICONS.put(Biomes.SOUL_SAND_VALLEY, Items.SOUL_SAND);
        SINGLE_BIOME_ICONS.put(Biomes.BASALT_DELTAS, Items.BASALT);
        SINGLE_BIOME_ICONS.put(Biomes.SULFUR_CAVES, Items.SULFUR);
        SINGLE_BIOME_ICONS.put(Biomes.PALE_GARDEN, Items.PALE_OAK_LOG);

        BIOMES_ICONS.put(BiomeTags.BADLANDS, Items.TERRACOTTA);
        BIOMES_ICONS.put(BiomeTags.BIRCH_FOREST, Items.BIRCH_LOG);
        BIOMES_ICONS.put(BiomeTags.COLD_ICE, Items.PACKED_ICE);
        BIOMES_ICONS.put(BiomeTags.COLD_SNOW, Items.SNOW_BLOCK);
        BIOMES_ICONS.put(BiomeTags.DARK_OAK_FOREST, Items.DARK_OAK_LOG);
        BIOMES_ICONS.put(BiomeTags.END, Items.END_STONE);
        BIOMES_ICONS.put(BiomeTags.JUNGLE_FOREST, Items.JUNGLE_LOG);
        BIOMES_ICONS.put(BiomeTags.MOUNTAIN, Items.STONE);
        BIOMES_ICONS.put(BiomeTags.OAK_FOREST, Items.OAK_LOG);
        BIOMES_ICONS.put(BiomeTags.SAVANNA_FOREST, Items.ACACIA_LOG);
        BIOMES_ICONS.put(BiomeTags.SURFACE, Items.MOSS_BLOCK);
        BIOMES_ICONS.put(BiomeTags.SWAMP, Items.SLIME_BALL);
        BIOMES_ICONS.put(BiomeTags.TAIGA_FOREST, Items.SPRUCE_LOG);
        BIOMES_ICONS.put(BiomeTags.WARM_DRY, Items.SAND);
        BIOMES_ICONS.put(BiomeTags.WARM_WATER, Items.KELP);
        BIOMES_ICONS.put(BiomeTags.WATER, Items.WATER_BUCKET);
    }
}
