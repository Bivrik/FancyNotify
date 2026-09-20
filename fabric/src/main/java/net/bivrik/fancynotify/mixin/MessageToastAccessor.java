package net.bivrik.fancynotify.mixin;

import de.dafuqs.spectrum.progression.toast.MessageToast;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MessageToast.class)
public abstract class MessageToastAccessor implements net.bivrik.fancynotify.compat.spectrum.MessageToastAccessor {
    @Accessor("titleText")
    public abstract Component getTitleText();

    @Accessor("messageText")
    public abstract Component getMessageText();

    @Accessor("itemStack")
    public abstract ItemStack getItemStack();

    @Accessor("soundEvent")
    public abstract SoundEvent getSoundEvent();
}
