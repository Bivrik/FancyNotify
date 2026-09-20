package net.bivrik.fancynotify.mixin;

import com.evandev.fieldguide.client.gui.toasts.FieldGuideToast;
import net.bivrik.fancynotify.compat.fieldguide.FieldGuideToastAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FieldGuideToast.class)
public abstract class FieldGuideAccessor implements FieldGuideToastAccessor {
    @Accessor("variantId")
    public abstract String getVariantId();

    @Accessor("entry")
    public abstract Object getEntry();
}
