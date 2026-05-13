package net.bivrik.fancynotify.mixin.accessor;

import net.bivrik.fancynotify.ITutorialToastAccessor;
import net.bivrik.fancynotify.IWeighedSoundEventsAccessor;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.client.sounds.Weighted;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(TutorialToast.class)
public abstract class TutorialToastMixinAccessor implements ITutorialToastAccessor {
    @Accessor("icon")
    public abstract TutorialToast.Icons getIcon();

    @Accessor("title")
    public abstract Component getTitle();

    @Accessor("message")
    public abstract Component getDescription();
}
