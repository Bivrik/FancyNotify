package net.bivrik.fancynotify.mixin.accessor;

import de.dafuqs.spectrum.progression.toast.RevelationToast;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RevelationToast.class)
public abstract class RevelationToastAccessor implements net.bivrik.fancynotify.compat.spectrum.RevelationToastAccessor {
    @Accessor("itemStack")
    public abstract ItemStack getItemStack();

    @Accessor("soundEvent")
    public abstract SoundEvent getSoundEvent();
}
