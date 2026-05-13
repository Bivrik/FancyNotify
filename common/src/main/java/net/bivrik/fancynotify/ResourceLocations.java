package net.bivrik.fancynotify;

import net.bivrik.fancynotify.core.Constants;
import net.minecraft.resources.ResourceLocation;

public final class ResourceLocations {
    private ResourceLocations() {}

    private static final String NAMESPACE = Constants.MOD_ID;

    public static ResourceLocation of(String path) {
        return ResourceLocation.fromNamespaceAndPath(NAMESPACE, path);
    }
}
