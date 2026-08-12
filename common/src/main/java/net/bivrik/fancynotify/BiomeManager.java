package net.bivrik.fancynotify;

import net.bivrik.fancynotify.gui.BiomeNotification;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class BiomeManager {
    // maybe check biome specific first
    // then check for tags
    // and in the end to default icon if nothing is found
    private static final Map<TagKey<Biome>, ItemStack> TAGS_ICONS = Map.ofEntries(
            Map.entry(BiomeTags.IS_FOREST, new ItemStack(Items.OAK_SAPLING)),
            Map.entry(BiomeTags.IS_BADLANDS, new ItemStack(Items.TERRACOTTA)),
            Map.entry(BiomeTags.IS_BEACH, new ItemStack(Items.SAND)),
            Map.entry(BiomeTags.IS_END, new ItemStack(Items.END_STONE)),
            Map.entry(BiomeTags.IS_HILL, new ItemStack(Items.GRAVEL)),
            Map.entry(BiomeTags.IS_JUNGLE, new ItemStack(Items.JUNGLE_SAPLING)),
            Map.entry(BiomeTags.IS_MOUNTAIN, new ItemStack(Items.STONE)),
            Map.entry(BiomeTags.IS_NETHER, new ItemStack(Items.NETHERRACK)),
            Map.entry(BiomeTags.IS_OCEAN, new ItemStack(Items.KELP)),
            Map.entry(BiomeTags.IS_RIVER, new ItemStack(Items.WATER_BUCKET)),
            Map.entry(BiomeTags.IS_SAVANNA, new ItemStack(Items.ACACIA_SAPLING)),
            Map.entry(BiomeTags.IS_TAIGA, new ItemStack(Items.SPRUCE_SAPLING)),
            Map.entry(BiomeTags.ALLOWS_SURFACE_SLIME_SPAWNS, new ItemStack(Items.SLIME_BALL))
    );
    private static final ItemStack DEFAULT_ICON = new ItemStack(Items.GRASS_BLOCK);
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
            ResourceLocation biomeId = biomeHolder.unwrap().map(ResourceKey::location, null);
            Component biomeName = getBiomeComponent(biomeId, currentBiome);
            ItemStack icon = DEFAULT_ICON;
            for (var entry : TAGS_ICONS.entrySet()) {
                if (biomeHolder.is(entry.getKey())) {
                    icon = entry.getValue();
                    break;
                }
            }
            notificationManager.add(new BiomeNotification(notificationManager, biomeName, icon));
        }
    }

    private Component getBiomeComponent(ResourceLocation id, Biome biome) {
        if (id == null) {
            return Component.translatable("fancynotify.gui.biome.unknown", Component.literal(biome.toString()));
        }
        return locationToTitle(id);
    }

    private @NotNull Component locationToTitle(ResourceLocation location) {
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
