package com.moulberry.flashback.mixin.replay_server;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.moulberry.flashback.playback.ReplayServer;
import net.minecraft.network.protocol.login.ClientboundGameProfilePacket;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Mixin(value = ServerLoginPacketListenerImpl.class, priority = 2000)
public abstract class MixinServerLoginPacketListenerImpl {

    @Shadow
    @Final
    MinecraftServer server;

    @Shadow
    ServerLoginPacketListenerImpl.State state;
    
    @Shadow
    @Nullable
    GameProfile gameProfile;

    @Inject(method = "handleHello", at = @At("HEAD"), cancellable = true)
    public void handleHello(ServerboundHelloPacket serverboundHelloPacket, CallbackInfo ci) {
        if (this.server instanceof ReplayServer) {
            UUID replayViewerUUID = UUID.nameUUIDFromBytes(serverboundHelloPacket.name().getBytes(StandardCharsets.UTF_8));
            GameProfile gameProfile = new GameProfile(replayViewerUUID, ReplayServer.REPLAY_VIEWER_NAME);
            gameProfile.getProperties().put("IsReplayViewer", new Property("IsReplayViewer", "True"));
            this.gameProfile = gameProfile;
            this.state = ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
            ci.cancel();
        }
    }

@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void onTick(CallbackInfo ci) {
        if (this.server instanceof ReplayServer) {
            if (this.state == ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT) {
                this.state = ServerLoginPacketListenerImpl.State.ACCEPTED;
            }
            // Si Sinytra nos tiene bloqueados aquí, saltamos directamente al inicio
            if (this.state == ServerLoginPacketListenerImpl.State.ACCEPTED) {
                // Este método es el que Minecraft llama para meter al jugador al mundo
                this.verifyLoginAndFinishConnection(); 
                ci.cancel();
            }
        }
    }

    // Los métodos comentados se pueden quedar así o eliminarse
}
