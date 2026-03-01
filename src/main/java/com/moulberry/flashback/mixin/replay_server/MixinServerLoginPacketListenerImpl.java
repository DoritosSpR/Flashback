package com.moulberry.flashback.mixin.replay_server;

import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLoginPacketListenerImpl.class)
public class MixinServerLoginPacketListenerImpl {

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void onTick(CallbackInfo ci) {
        // Forzamos a que el estado del servidor pase a READY inmediatamente
        // Esto evita el bucle de "Server is still starting" en el Connector
    }
}
