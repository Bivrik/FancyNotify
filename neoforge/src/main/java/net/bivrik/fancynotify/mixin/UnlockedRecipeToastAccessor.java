package net.bivrik.fancynotify.mixin;

import de.dafuqs.spectrum.progression.toast.UnlockedRecipeToast;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(UnlockedRecipeToast.class)
public abstract class UnlockedRecipeToastAccessor implements net.bivrik.compat.spectrum.UnlockedRecipeToastAccessor {
    @Accessor("title")
    public abstract Component getTitle();

    @Accessor("text")
    public abstract Component getText();

    @Accessor("itemStacks")
    public abstract List<ItemStack> getItemStacks();

    @Accessor("soundEvent")
    public abstract SoundEvent getSoundEvent();
}
