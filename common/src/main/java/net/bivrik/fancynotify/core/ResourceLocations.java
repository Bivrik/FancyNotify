package net.bivrik.fancynotify.core;

import net.minecraft.resources.ResourceLocation;

public final class ResourceLocations {
    private ResourceLocations() {}

    public static ResourceLocation of(String path) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path);
    }
}
