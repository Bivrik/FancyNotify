package net.bivrik.fancynotify.utility;

import net.bivrik.fancynotify.core.Constants;
import net.minecraft.resources.Identifier;

public final class Identifiers {
    private Identifiers() {}

    public static Identifier of(String path) {
        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, path);
    }
}
