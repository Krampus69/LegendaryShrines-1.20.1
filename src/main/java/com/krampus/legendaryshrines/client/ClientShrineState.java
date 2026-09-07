package com.krampus.legendaryshrines.client;

import com.krampus.legendaryshrines.block.ShrineBlock;
import com.krampus.legendaryshrines.data.ShrineBind;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;

public final class ClientShrineState {

    @Nullable
    private static ShrineBind bind;
    private static long boundAt = Long.MIN_VALUE;
    private static long cooldownUntil;

    public static void set(@Nullable ShrineBind next, boolean activate, long cooldown) {
        bind = next;
        cooldownUntil = cooldown;

        if (next == null || !activate) {
            return;
        }

        ShrineSweep.start(next);
        ClientLevel level = Minecraft.getInstance().level;
        boundAt = level == null ? Long.MIN_VALUE : level.getGameTime();
    }

    public static void clearOnDisconnect() {
        bind = null;
        boundAt = Long.MIN_VALUE;
        cooldownUntil = 0L;
        ShrineSweep.stop();
    }

    public static long ticksSinceBind(long gameTime) {
        return boundAt == Long.MIN_VALUE ? Long.MAX_VALUE : gameTime - boundAt;
    }

    public static boolean canRespawnAtShrine() {
        if (boundPosHere() == null) {
            return false;
        }

        ClientLevel level = Minecraft.getInstance().level;
        return level != null && level.getGameTime() >= cooldownUntil;
    }

    @Nullable
    public static BlockPos litPos() {
        BlockPos pos = boundPosHere();
        if (pos == null) {
            return null;
        }

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null || !level.isLoaded(pos)) {
            return null;
        }
        if (!(level.getBlockState(pos).getBlock() instanceof ShrineBlock)) {
            return null;
        }
        return pos;
    }

    @Nullable
    private static BlockPos boundPosHere() {
        Minecraft minecraft = Minecraft.getInstance();
        if (bind == null || minecraft.level == null) {
            return null;
        }
        if (!minecraft.level.dimension().equals(bind.dimension())) {
            return null;
        }
        return bind.pos();
    }
}
