package net.bivrik.fancynotify;

import net.bivrik.fancynotify.core.Log;
import net.bivrik.fancynotify.notification.NotificationManager;
import net.bivrik.fancynotify.notification.gui.MusicNotification;
import net.bivrik.fancynotify.utility.ResourceLocations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class MusicManager {
    private static final Component FALLBACK_MESSAGE = Component.translatable("fancynotify.gui.music.message");

    private final Options options;
    private final NotificationManager notificationManager;

    public MusicManager(Options options, NotificationManager notificationManager) {
        this.options = options;
        this.notificationManager = notificationManager;
    }

    public void onStartedPlaying(ResourceLocation musicId) {
        if (options.getSoundSourceVolume(SoundSource.MUSIC) <= 0.0f) {
            return;
        }

        // minecraft.music.game.clark -> fancynotify.music.game.clark -> C418 - Clark
        // coolassmod.music.end.no_escape -> 1) End No Escape; 2) MTQ - No Escape
        if (musicId.getNamespace().equals("minecraft")) { // handle vanilla ones
            ResourceLocation nonVanillaMusicId = ResourceLocations.of(musicId.getPath());
            String key = nonVanillaMusicId.toLanguageKey().replace("/", ".");
            Component musicName = Component.translatable(key);

            String[] musicInfo = musicName.getString().split(" - ");
            if (musicInfo.length == 2) { // if I didn't forget to add translation
                Component artist = Component.literal(musicInfo[0]);
                Component title = Component.literal(musicInfo[1]);
                notificationManager.add(new MusicNotification(notificationManager, artist, title));
            } else { // if there is missing some translations just do whatever
                notificationManager.add(new MusicNotification(notificationManager, musicName, FALLBACK_MESSAGE));
                Log.warn("No translation provided for {} for music notification", musicId);
            }
        } else { // modded if there is any added music
            String key = musicId.getPath().replace("/", ".");
            Component title;
            if (Language.getInstance().has(key)) { // get translation for it if there is one
                title = Component.translatable(key);
            } else { // else just get the visual appealing one
                String[] parts = key.split("\\.");
                StringBuilder builder = new StringBuilder();
                for (int i = Math.max(parts.length - 2, 0); i < parts.length; i++) {
                    String[] words = parts[i].split("_");
                    for (int j = 0; j < words.length; j++) {
                        builder.append(words[j].substring(0, 1).toUpperCase()).append(words[j].substring(1));
                        if (j < words.length - 1) {
                            builder.append(" ");
                        }
                    }
                    if (i < parts.length - 1) {
                        builder.append(" ");
                    }
                }
                title = Component.literal(builder.toString());
            }
            notificationManager.add(new MusicNotification(notificationManager, title, FALLBACK_MESSAGE));
        }
    }
}
