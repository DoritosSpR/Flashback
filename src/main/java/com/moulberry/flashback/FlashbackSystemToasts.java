package com.moulberry.flashback;

import net.minecraft.client.gui.components.toasts.SystemToast;

public class FlashbackSystemToasts {
    public static SystemToast.SystemToastIds RECORDING_TOAST;

    static {
        try {
            // Intentamos obtener la constante (esto falla en Forge/Connector)
            RECORDING_TOAST = SystemToast.SystemToastIds.valueOf("RECORDING_TOAST");
        } catch (Exception e) {
            // Si falla, usamos una notificación estándar de Minecraft para evitar el crash
            RECORDING_TOAST = SystemToast.SystemToastIds.PERIODIC_NOTIFICATION;
        }
    }
}
