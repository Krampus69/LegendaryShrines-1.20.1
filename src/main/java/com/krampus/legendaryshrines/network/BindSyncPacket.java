package com.krampus.legendaryshrines.network;

import com.krampus.legendaryshrines.LegendaryShrines;
import com.krampus.legendaryshrines.client.ClientShrineState;
import com.krampus.legendaryshrines.data.ShrineBind;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import javax.annotation.Nullable;

public record BindSyncPacket(@Nullable ShrineBind bind, boolean activate, long cooldownUntil)
        implements CustomPacketPayload {

    public static final Type<BindSyncPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(LegendaryShrines.MOD_ID, "bind_sync"));

    public static final StreamCodec<FriendlyByteBuf, BindSyncPacket> STREAM_CODEC =
            StreamCodec.of(BindSyncPacket::write, BindSyncPacket::read);

    private static void write(FriendlyByteBuf buf, BindSyncPacket packet) {
        buf.writeBoolean(packet.activate);
        buf.writeLong(packet.cooldownUntil);
        buf.writeBoolean(packet.bind != null);
        if (packet.bind != null) {
            buf.writeBlockPos(packet.bind.pos());
            buf.writeResourceKey(packet.bind.dimension());
        }
    }

    private static BindSyncPacket read(FriendlyByteBuf buf) {
        boolean activate = buf.readBoolean();
        long cooldownUntil = buf.readLong();
        ShrineBind bind = null;
        if (buf.readBoolean()) {
            BlockPos pos = buf.readBlockPos();
            ResourceKey<Level> dimension = buf.readResourceKey(Registries.DIMENSION);
            bind = new ShrineBind(pos, dimension);
        }
        return new BindSyncPacket(bind, activate, cooldownUntil);
    }

    public static void handle(BindSyncPacket packet, IPayloadContext context) {
        ClientShrineState.set(packet.bind, packet.activate, packet.cooldownUntil);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
