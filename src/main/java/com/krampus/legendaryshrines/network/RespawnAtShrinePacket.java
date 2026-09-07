package com.krampus.legendaryshrines.network;

import com.krampus.legendaryshrines.data.ShrineBinding;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RespawnAtShrinePacket {

    public RespawnAtShrinePacket() {
    }

    public RespawnAtShrinePacket(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player == null || player.isAlive()) {
                return;
            }
            if (ShrineBinding.get(player) == null) {
                return;
            }
            if (player.level().getGameTime() < ShrineBinding.getCooldownUntil(player)) {
                return;
            }
            ShrineBinding.setRespawnPending(player);
        });
        ctx.setPacketHandled(true);
    }
}
