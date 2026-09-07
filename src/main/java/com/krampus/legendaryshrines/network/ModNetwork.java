package com.krampus.legendaryshrines.network;

import com.krampus.legendaryshrines.LegendaryShrines;
import com.krampus.legendaryshrines.data.ShrineBind;
import com.krampus.legendaryshrines.data.ShrineBinding;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import javax.annotation.Nullable;

public final class ModNetwork {

    private static final String PROTOCOL = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(LegendaryShrines.MOD_ID, "main"),
            () -> PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals);


    public static void register() {
        int id = 0;

        CHANNEL.messageBuilder(BindSyncPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(BindSyncPacket::encode)
                .decoder(BindSyncPacket::new)
                .consumerMainThread(BindSyncPacket::handle)
                .add();

        CHANNEL.messageBuilder(RespawnAtShrinePacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(RespawnAtShrinePacket::encode)
                .decoder(RespawnAtShrinePacket::new)
                .consumerMainThread(RespawnAtShrinePacket::handle)
                .add();
    }

    public static void syncBind(ServerPlayer player, @Nullable ShrineBind bind, boolean activate) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                new BindSyncPacket(bind, activate, ShrineBinding.getCooldownUntil(player)));
    }
}
