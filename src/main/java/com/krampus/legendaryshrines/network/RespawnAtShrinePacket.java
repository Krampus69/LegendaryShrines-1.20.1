package com.krampus.legendaryshrines.network;

import com.krampus.legendaryshrines.LegendaryShrines;
import com.krampus.legendaryshrines.data.ShrineBinding;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record RespawnAtShrinePacket() implements CustomPacketPayload {

    public static final Type<RespawnAtShrinePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(LegendaryShrines.MOD_ID, "respawn_at_shrine"));

    public static final StreamCodec<FriendlyByteBuf, RespawnAtShrinePacket> STREAM_CODEC =
            StreamCodec.unit(new RespawnAtShrinePacket());

    public static void handle(RespawnAtShrinePacket packet, IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayer player)) {
            return;
        }
        if (player.isAlive() || player.server.isHardcore()) {
            return;
        }
        if (ShrineBinding.get(player) == null) {
            return;
        }
        if (player.level().getGameTime() < ShrineBinding.getCooldownUntil(player)) {
            return;
        }
        ShrineBinding.setRespawnPending(player);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
