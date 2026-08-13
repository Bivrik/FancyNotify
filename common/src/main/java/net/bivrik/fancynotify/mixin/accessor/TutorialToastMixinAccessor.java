package net.bivrik.fancynotify.mixin.accessor;

import net.bivrik.fancynotify.accessor.ITutorialToastAccessor;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TutorialToast.class)
public abstract class TutorialToastMixinAccessor implements ITutorialToastAccessor {
    @Accessor("icon")
    public abstract TutorialToast.Icons getIcon();

    @Accessor("title")
    public abstract Component getTitle();

    @Accessor("message")
    public abstract Component getDescription();
}
