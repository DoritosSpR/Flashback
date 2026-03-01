package com.moulberry.flashback.mixin.replay_server;

import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLoginPacketListenerImpl.class)
public abstract class MixinServerLoginPacketListenerImpl {

    @Shadow private ServerLoginPacketListenerImpl.State state;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void onTick(CallbackInfo ci) {
        // Si el estado es "READY_TO_ACCEPT" (el que suele colgarse en Connector),
        // lo forzamos a saltar directamente a la fase de juego.
        if (this.state.toString().equals("READY_TO_ACCEPT") || this.state.toString().equals("ACCEPTED")) {
            this.handleGameProfile(); 
            ci.cancel();
        }
    }

    @Shadow
    public abstract void handleGameProfile();
}
