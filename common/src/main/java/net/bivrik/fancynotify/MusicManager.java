package net.bivrik.fancynotify;

import net.bivrik.fancynotify.core.Log;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.notification.gui.MusicNotification;
import net.bivrik.fancynotify.utility.Identifiers;
import net.minecraft.client.Options;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MusicManager {
    private static final Component FALLBACK_MESSAGE = Component.translatable("fancynotify.gui.music.message");

    private final Options options;
    private final NotificationManager notificationManager;

    public MusicManager(Options options, NotificationManager notificationManager) {
        this.options = options;
        this.notificationManager = notificationManager;
    }

    public void onStartedPlaying(Identifier musicId) {
        if (options.getSoundSourceVolume(SoundSource.MUSIC) <= 0.0f) {
            return;
        }

        // minecraft:music/game/clark -> fancynotify:music/game/clark -> C418 - Clark
        // coolassmod:music/end/no_escape -> 1) End No Escape; 2) MTQ - No Escape;

        // handle vanilla ones
        if (musicId.getNamespace().equals("minecraft")) {
            Identifier vanillaMusicId = Identifiers.of(musicId.getPath());
            String key = getKey(vanillaMusicId);
            Component musicTitle = Component.translatable(key);

            String[] musicInfo = musicTitle.getString().split(" - ");
            // if I didn't forget to add translation
            if (musicInfo.length == 2) {
                Component artist = Component.literal(musicInfo[0]);
                Component title = Component.literal(musicInfo[1]);
                notificationManager.add(new MusicNotification(notificationManager, artist, title));
            }
            // if there is missing some translations just get visual appealing one
            else {
                Log.warn("No translation provided for {} for music notification", musicId);
                Component title = Component.literal(getTitleFromId(musicId));
                notificationManager.add(new MusicNotification(notificationManager, title, FALLBACK_MESSAGE));
            }
        }
        // handle modded
        else {
            Component title;
            String key = getKey(musicId);
            // get translation for it if there is one
            if (Language.getInstance().has(key)) {
                title = Component.translatable(key);
            }
            // else just get the visual appealing one
            else {
                title = Component.literal(getTitleFromId(musicId));
            }
            notificationManager.add(new MusicNotification(notificationManager, title, FALLBACK_MESSAGE));
        }
    }

    private String getKey(Identifier id) {
        return id.toLanguageKey().replace('/', '.');
    }

    // Example: something:oops/omg/cool_string -> oops omg cool_string -> Omg Cool String
    private String getTitleFromId(Identifier id) {
        String[] pathParts = id.getPath().split("/");

        int wordsToInclude = 2;
        int startInclusive = Math.max(pathParts.length - wordsToInclude, 0);

        return Arrays.stream(pathParts, startInclusive, pathParts.length)
                .filter(part -> !part.isEmpty())
                .map(this::toTitleCase)
                .collect(Collectors.joining(" "));
    }

    // Just over-engineered because I want to, lol.
    // Example: cool_string test -> Cool String Test
    private String toTitleCase(String s) {
        if (s == null || s.isBlank()) {
            return "";
        }
        StringBuilder result = new StringBuilder(s.length());
        boolean shouldCapitalize = true;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '_' || Character.isWhitespace(c)) {
                shouldCapitalize = true;
            } else {
                if (shouldCapitalize) {
                    if (!result.isEmpty()) {
                        result.append(' ');
                    }
                    result.append(Character.toUpperCase(c));
                    shouldCapitalize = false;
                } else {
                    result.append(c);
                }
            }
        }
        return result.toString();
    }
}
