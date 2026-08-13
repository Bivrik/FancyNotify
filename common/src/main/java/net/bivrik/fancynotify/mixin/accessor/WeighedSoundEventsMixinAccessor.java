package net.bivrik.fancynotify.mixin.accessor;

import net.bivrik.fancynotify.accessor.IWeighedSoundEventsAccessor;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.client.sounds.Weighted;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(WeighedSoundEvents.class)
public abstract class WeighedSoundEventsMixinAccessor implements IWeighedSoundEventsAccessor {
    @Accessor("list")
    public abstract List<Weighted<Sound>> getWeightedSounds();
}
