package com.krampus.legendaryshrines.network;

import com.krampus.legendaryshrines.client.ClientShrineState;
import com.krampus.legendaryshrines.data.ShrineBind;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class BindSyncPacket {

    @Nullable
    private final ShrineBind bind;
    private final boolean activate;
    private final long cooldownUntil;

    public BindSyncPacket(@Nullable ShrineBind bind, boolean activate, long cooldownUntil) {
        this.bind = bind;
        this.activate = activate;
        this.cooldownUntil = cooldownUntil;
    }

    public BindSyncPacket(FriendlyByteBuf buf) {
        this.activate = buf.readBoolean();
        this.cooldownUntil = buf.readLong();
        if (buf.readBoolean()) {
            BlockPos pos = buf.readBlockPos();
            ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, buf.readResourceLocation());
            this.bind = new ShrineBind(pos, dimension);
        } else {
            this.bind = null;
        }
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.activate);
        buf.writeLong(this.cooldownUntil);
        buf.writeBoolean(this.bind != null);
        if (this.bind != null) {
            buf.writeBlockPos(this.bind.pos());
            buf.writeResourceLocation(this.bind.dimension().location());
        }
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> ClientShrineState.set(this.bind, this.activate, this.cooldownUntil)));
        ctx.setPacketHandled(true);
    }
}
