package com.moulberry.flashback.mixin.replay_server;

import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerLoginPacketListenerImpl.class, priority = 1001)
public abstract class MixinServerLoginPacketListenerImpl {

    @Shadow private ServerLoginPacketListenerImpl.State state;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void onTick(CallbackInfo ci) {
        // Forzamos el estado a ACCEPTED para saltarnos el "Server is still starting"
        if (this.state != ServerLoginPacketListenerImpl.State.ACCEPTED) {
            try {
                this.handleGameProfile();
                ci.cancel();
            } catch (Exception ignored) {
                // Si falla, es que el perfil aún no está listo, esperamos al siguiente tick
            }
        }
    }

    @Shadow public abstract void handleGameProfile();
}
