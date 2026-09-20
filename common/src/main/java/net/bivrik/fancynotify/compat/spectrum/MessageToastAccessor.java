package net.bivrik.fancynotify.compat.spectrum;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;

public interface MessageToastAccessor {
    Component getTitleText();
    Component getMessageText();
    ItemStack getItemStack();
    SoundEvent getSoundEvent();
}
