package net.bivrik.fancynotify;

import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.sounds.Weighted;

import java.util.List;

public interface IWeighedSoundEventsAccessor {
    List<Weighted<Sound>> getWeightedSounds();
}
