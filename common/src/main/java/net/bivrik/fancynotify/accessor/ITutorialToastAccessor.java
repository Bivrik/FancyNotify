package net.bivrik.fancynotify.accessor;

import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.network.chat.Component;

public interface ITutorialToastAccessor {
    TutorialToast.Icons getIcon();
    Component getTitle();
    Component getDescription();
}
