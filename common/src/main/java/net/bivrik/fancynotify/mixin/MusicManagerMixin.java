package net.bivrik.fancynotify.mixin;

import net.bivrik.fancynotify.NotificationManager;
import net.bivrik.fancynotify.core.Common;
import net.bivrik.fancynotify.gui.MusicNotification;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MusicManager.class)
public class MusicManagerMixin {
    @Shadow
    private SoundInstance currentMusic;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(at = @At("RETURN"), method = "startPlaying")
    public void onStartedPlaying(Music selector, CallbackInfo info) {
        if (this.currentMusic != null && this.minecraft.options.getSoundSourceVolume(SoundSource.MUSIC) > 0) {
            Sound musicSound = this.currentMusic.getSound();
            if (musicSound != SoundManager.EMPTY_SOUND) {
                Component musicName = Component.translatable(musicSound.getLocation().toShortLanguageKey().replace("/", "."));

                String[] musicInfo = musicName.getString().split(" - ");
                Component artist = Component.literal(musicInfo[0]);
                Component title = Component.literal(musicInfo[1]);

                NotificationManager manager = Common.getNotificationManager();
                manager.add(new MusicNotification(manager, artist, title));
            }
        }
    }
}
