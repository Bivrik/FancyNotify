package net.bivrik.compat.spectrum;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface UnlockedRecipeToastAccessor {
    Component getTitle();
    Component getText();
    List<ItemStack> getItemStacks();
    SoundEvent getSoundEvent();
}
