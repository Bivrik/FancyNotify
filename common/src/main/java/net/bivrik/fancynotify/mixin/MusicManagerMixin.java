package net.bivrik.fancynotify.mixin;

import net.bivrik.fancynotify.FancyNotify;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.Music;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MusicManager.class)
public class MusicManagerMixin {
    @Shadow
    private SoundInstance currentMusic;

    @Inject(at = @At("RETURN"), method = "startPlaying")
    public void onStartedPlaying(Music music, CallbackInfo info) {
        if (this.currentMusic != null) {
            Sound musicSound = this.currentMusic.getSound();
            if (musicSound != null & musicSound != SoundManager.EMPTY_SOUND) {
                Identifier musicId = musicSound.getLocation();
                FancyNotify.getInstance().getMusicManager().onStartedPlaying(musicId);
            }
        }
    }
}
