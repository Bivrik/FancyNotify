package net.bivrik.fancynotify.compat.spectrum;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;

public interface RevelationToastAccessor {
    ItemStack getItemStack();
    SoundEvent getSoundEvent();
}
