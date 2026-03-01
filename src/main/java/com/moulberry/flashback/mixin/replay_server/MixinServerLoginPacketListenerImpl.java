package com.moulberry.flashback.mixin.replay_server;

import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLoginPacketListenerImpl.class)
public abstract class MixinServerLoginPacketListenerImpl {

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void onTick(CallbackInfo ci) {
        // En Forge/Connector, el estado de autenticación a veces no avanza.
        // Forzamos el login si detectamos que estamos en el servidor de replay.
        try {
            this.handleGameProfile();
            ci.cancel();
        } catch (Exception ignored) {
            // Si aún no está listo para procesar el perfil, dejamos que siga el tick normal
        }
    }

    @Shadow
    public abstract void handleGameProfile();
}
