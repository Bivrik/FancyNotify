package net.bivrik.fancynotify;

import net.bivrik.fancynotify.core.Log;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class SplashesManager {
    private static final Logger LOGGER = Log.getSpecificLogger(SplashesManager.class);

    private static final ResourceLocation SPLASHES_LOCATION = ResourceLocations.of("splashes.txt");
    private static final Random RANDOM = new Random();

    private final List<String> splashes = new ArrayList<>();

    public SplashesManager(Minecraft minecraft) {
        readSplashes(minecraft.getResourceManager());
    }

    private void readSplashes(ResourceManager resourceManager) {
        Optional<Resource> optionalResource = resourceManager.getResource(SPLASHES_LOCATION);
        if (optionalResource.isEmpty()) {
            LOGGER.warn("Could not read splashes, because there are no splashes");
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(optionalResource.get().openAsReader());
            splashes.addAll(reader.lines().toList());
        } catch (IOException e) {
            LOGGER.error("Could not read splashes:", e);
        }
    }

    public String getSplash() {
        if (splashes.isEmpty()) {
            LOGGER.warn("Could not get a splash, because splashes are empty");
            return "";
        }

        return splashes.get(RANDOM.nextInt(splashes.size()));
    }
}
