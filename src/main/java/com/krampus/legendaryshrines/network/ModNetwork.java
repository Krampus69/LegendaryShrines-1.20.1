package com.krampus.legendaryshrines.network;

import com.krampus.legendaryshrines.data.ShrineBind;
import com.krampus.legendaryshrines.data.ShrineBinding;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import javax.annotation.Nullable;

public final class ModNetwork {

    private static final String PROTOCOL = "1";

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(PROTOCOL);

        registrar.playToClient(BindSyncPacket.TYPE, BindSyncPacket.STREAM_CODEC, BindSyncPacket::handle);
        registrar.playToServer(RespawnAtShrinePacket.TYPE, RespawnAtShrinePacket.STREAM_CODEC, RespawnAtShrinePacket::handle);
    }

    public static void syncBind(ServerPlayer player, @Nullable ShrineBind bind, boolean activate) {
        PacketDistributor.sendToPlayer(player,
                new BindSyncPacket(bind, activate, ShrineBinding.getCooldownUntil(player)));
    }
}
