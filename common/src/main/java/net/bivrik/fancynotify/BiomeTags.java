package net.bivrik.fancynotify;

import net.bivrik.fancynotify.utility.Identifiers;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public final class BiomeTags {
    private BiomeTags() {}

    private static TagKey<Biome> create(String id) {
        return TagKey.create(Registries.BIOME, Identifiers.of(id));
    }

    public static TagKey<Biome> BADLANDS = create("badlands");
    public static TagKey<Biome> BIRCH_FOREST = create("birch_forest");
    public static TagKey<Biome> COLD_ICE = create("cold_ice");
    public static TagKey<Biome> COLD_SNOW = create("cold_snow");
    public static TagKey<Biome> DARK_OAK_FOREST = create("dark_oak_forest");
    public static TagKey<Biome> END = create("end");
    public static TagKey<Biome> JUNGLE_FOREST = create("jungle_forest");
    public static TagKey<Biome> MOUNTAIN = create("mountain");
    public static TagKey<Biome> OAK_FOREST = create("oak_forest");
    public static TagKey<Biome> SAVANNA_FOREST = create("savanna_forest");
    public static TagKey<Biome> SURFACE = create("surface");
    public static TagKey<Biome> SWAMP = create("swamp");
    public static TagKey<Biome> TAIGA_FOREST = create("taiga_forest");
    public static TagKey<Biome> WARM_DRY = create("warm_dry");
    public static TagKey<Biome> WARM_WATER = create("warm_water");
    public static TagKey<Biome> WATER = create("water");
}
