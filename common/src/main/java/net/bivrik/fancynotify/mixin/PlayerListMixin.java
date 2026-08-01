package net.bivrik.fancynotify.mixin;

import net.bivrik.fancynotify.NotificationManager;
import net.bivrik.fancynotify.core.Common;
import net.bivrik.fancynotify.gui.PlayerLoginNotification;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class PlayerListMixin {
    // Maybe different entry point? And also add skin instead of wool duh
    @Inject(at = @At("RETURN"), method = "placeNewPlayer")
    public void onPlacedNewPlayer(Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo info) {
        NotificationManager manager = Common.getNotificationManager();
        manager.add(new PlayerLoginNotification(manager, player.getDisplayName()));
    }
}
