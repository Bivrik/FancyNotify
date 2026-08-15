package net.bivrik.fancynotify.credits;

import com.google.gson.Gson;
import net.bivrik.fancynotify.core.Log;
import net.bivrik.fancynotify.utility.JsonHelper;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class CreditsManager {
    private static final Logger LOGGER = Log.getSpecificLogger(CreditsManager.class);
    
    private static final String COMMON_CREDITS_URL = "https://cdn.jsdelivr.net/gh/Bivrik/ModsCredits@master/credits/common.json";
    private static final String CREDITS_URL = "https://cdn.jsdelivr.net/gh/Bivrik/ModsCredits@master/credits/mods/fancynotify.json";
    private static final int CACHE_VALIDITY_HOURS = 6;

    private static final String MAIN_FOLDER = "./bivrik/";
    private static final String MODS_FOLDER = MAIN_FOLDER + "mods/";
    private static final String COMMON_CREDITS_FILE = MAIN_FOLDER + "common.json";
    private static final String MODS_CREDITS_FILE = MODS_FOLDER + "fancynotify.json";

    private CreditsData credits;

    public CreditsManager() {
        loadAndCombineCreditsAsync().thenAccept(credits -> {
            if (credits == null) {
                this.credits = getHardcodedFallback();
            } else {
                this.credits = credits;
            }
        });
    }

    public CreditsData getCredits() {
        if (credits == null) {
            LOGGER.warn("Could not access credits because they are null. Hardcoded fallback");
            return getHardcodedFallback();
        }

        return credits;
    }

    private CompletableFuture<CreditsData> loadAndCombineCreditsAsync() {
        File mainFolder = new File(MAIN_FOLDER);
        if (mainFolder.mkdir()) {
            File creditsFolder = new File(MODS_FOLDER);
            creditsFolder.mkdir();
        }

        CompletableFuture<CreditsData> modCreditsFuture = CompletableFuture.supplyAsync(() -> loadCredits(MODS_CREDITS_FILE, CREDITS_URL));
        CompletableFuture<CreditsData> commonCreditsFuture = CompletableFuture.supplyAsync(() -> loadCredits(COMMON_CREDITS_FILE, COMMON_CREDITS_URL));

        return modCreditsFuture.thenCombine(commonCreditsFuture, (modCredits, commonCredits) -> {
            if (modCredits == null || commonCredits == null) {
                return null;
            }

            CreditsData credits = new CreditsData(modCredits.getCategories());
            for (var entry : commonCredits.getCategories().entrySet()) {
                credits.addCategory(entry.getKey(), entry.getValue());
            }
            return credits;
        }).exceptionally(e -> {
            LOGGER.error("Failed to load credits data:", e);
            return null;
        });
    }

    private CreditsData loadCredits(String path, String url) {
        LOGGER.info("Reading credits from cache '{}'...", path);

        boolean isDateOutdated = false;
        CreditsData fallback = null;
        File file = new File(path);
        if (file.exists()) {
            Optional<CreditsData> optionalCredits = JsonHelper.tryToRead(file, CreditsData.class);
            if (optionalCredits.isPresent()) {
                CreditsData credits = optionalCredits.get();

                Instant cacheDate = credits.getDate();
                Instant currentDate = Instant.now();

                if (ChronoUnit.HOURS.between(cacheDate, currentDate) <= CACHE_VALIDITY_HOURS + 1) {
                    LOGGER.info("Successfully read credits");
                    return credits;
                } else {
                    LOGGER.info("Cache is not valid anymore due to time");
                    isDateOutdated = true;
                    fallback = credits;
                }
            }
        } else {
            LOGGER.info("There is no cache yet");
        }

        LOGGER.info("Failed to read cache");

        CreditsData online = readOnlineCredits(path, url);
        if (online == null && isDateOutdated) {
            return fallback;
        } else {
            return online;
        }
    }

    private CreditsData readOnlineCredits(String path, String url) {
        LOGGER.info("Reading credits from host '{}'...", url);

        URI uri = URI.create(url);
        HttpResponse<String> response;
        String responseValue;

        // Getting a response from a host provided by a url
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(8))
                .build();

        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("User-Agent", "fancy-toasts")
                .header("Cache-Control", "no-cache")
                .build();

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            LOGGER.error("Failed to access host to get credits: {}", e.getMessage());
            return null;
        }

        // Verifying response and getting body (value) from it
        try {
            if (response.statusCode() != 200) {
                LOGGER.error("Failed response status: {}", response.statusCode());
                return null;
            }

            responseValue = response.body();
        } catch (Exception e) {
            LOGGER.error("Failed to access a response from host: {}", e.getMessage());
            return null;
        }

        // How and why
        if (responseValue == null) {
            LOGGER.error("Response equals null. Ugh...");
            return null;
        }

        // Save and return
        CreditsData credits = JsonHelper.GSON.fromJson(responseValue, CreditsData.class);
        credits.saveDate(Instant.now());
        File jsonFile = new File(path);
        try (FileWriter writer = new FileWriter(jsonFile)) {
            Gson gson = new Gson();
            gson.toJson(credits, writer);
        } catch (Exception e) {
            LOGGER.error("Could not write json file {}: {}", jsonFile.getName(), e.getMessage());
        }

        LOGGER.info("Successfully read credits");
        return credits;
    }

    public CreditsData getHardcodedFallback() {
        LOGGER.info("Returning hardcoded fallback for credits");

        CreditsData data = new CreditsData();
        data.addCategory("credits_fallback", data.addUsers(
                data.createUser("Sadly, credits could not be reached.", ":("),
                data.createUser("It can be a poor Internet connection"),
                data.createUser("or some other error. Hopefully,"),
                data.createUser("it fixes itself", ":D")
        ));

        return data;
    }

    public static class CreditsData {
        private final Map<String, List<User>> categories;

        private String date;

        public CreditsData(Map<String, List<User>> categories) {
            this.categories = categories;
        }

        public CreditsData() {
            this(new HashMap<>());
        }

        public Map<String, List<User>> getCategories() {
            return new HashMap<>(categories);
        }

        public Instant getDate() {
            return Instant.parse(date);
        }

        public void saveDate(Instant date) {
            this.date = date.toString();
        }

        public void addCategory(String category, List<User> users) {
            this.categories.put(category, users);
        }

        public User createUser(String name, String annotation) {
            return new User(name, annotation);
        }

        public User createUser(String name) {
            return new User(name, null);
        }

        public List<User> addUsers(User... users) {
            return Arrays.asList(users);
        }

        public record User(String name, String annotation) {}
    }
}