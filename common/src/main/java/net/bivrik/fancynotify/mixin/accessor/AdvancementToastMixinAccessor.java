package net.bivrik.fancynotify.mixin.accessor;

import net.bivrik.fancynotify.accessor.IAdvancementHolderAccessor;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AdvancementToast.class)
public abstract class AdvancementToastMixinAccessor implements IAdvancementHolderAccessor {
    @Accessor("advancement")
    public abstract AdvancementHolder getAdvancementHolder();
}
