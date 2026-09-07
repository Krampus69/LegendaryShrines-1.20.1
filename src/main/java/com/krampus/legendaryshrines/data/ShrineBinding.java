package com.krampus.legendaryshrines.data;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public final class ShrineBinding {

    public static final String ROOT_TAG = "LegendaryShrines";
    private static final String BIND_TAG = "Bind";
    private static final String SUPPRESS_TAG = "Suppressed";
    private static final String PENDING_TAG = "RespawnPending";
    private static final String COOLDOWN_TAG = "CooldownUntil";
    private static final String FLOAT_TAG = "FloatUntil";


    private static CompoundTag root(Player player) {
        CompoundTag data = player.getPersistentData();
        if (!data.contains(ROOT_TAG)) {
            data.put(ROOT_TAG, new CompoundTag());
        }
        return data.getCompound(ROOT_TAG);
    }

    @Nullable
    public static ShrineBind get(Player player) {
        CompoundTag root = root(player);
        if (!root.contains(BIND_TAG)) {
            return null;
        }
        return ShrineBind.load(root.getCompound(BIND_TAG));
    }

    public static void set(Player player, ShrineBind bind) {
        root(player).put(BIND_TAG, bind.save());
    }

    public static void clear(Player player) {
        root(player).remove(BIND_TAG);
    }

    @Nullable
    public static BlockPos getSuppressed(Player player) {
        CompoundTag root = root(player);
        if (!root.contains(SUPPRESS_TAG)) {
            return null;
        }
        return BlockPos.of(root.getLong(SUPPRESS_TAG));
    }

    public static void setSuppressed(Player player, BlockPos pos) {
        root(player).putLong(SUPPRESS_TAG, pos.asLong());
    }

    public static void clearSuppressed(Player player) {
        root(player).remove(SUPPRESS_TAG);
    }

    public static boolean isRespawnPending(Player player) {
        return root(player).getBoolean(PENDING_TAG);
    }

    public static void setRespawnPending(Player player) {
        root(player).putBoolean(PENDING_TAG, true);
    }

    public static void clearRespawnPending(Player player) {
        root(player).remove(PENDING_TAG);
    }

    public static long getCooldownUntil(Player player) {
        return root(player).getLong(COOLDOWN_TAG);
    }

    public static void setCooldownUntil(Player player, long gameTime) {
        root(player).putLong(COOLDOWN_TAG, gameTime);
    }

    public static long getFloatUntil(Player player) {
        return root(player).getLong(FLOAT_TAG);
    }

    public static void setFloatUntil(Player player, long gameTime) {
        root(player).putLong(FLOAT_TAG, gameTime);
    }

    public static void clearFloatUntil(Player player) {
        root(player).remove(FLOAT_TAG);
    }

    public static void copy(Player from, Player to) {
        CompoundTag source = from.getPersistentData().getCompound(ROOT_TAG);
        to.getPersistentData().put(ROOT_TAG, source.copy());
    }
}
