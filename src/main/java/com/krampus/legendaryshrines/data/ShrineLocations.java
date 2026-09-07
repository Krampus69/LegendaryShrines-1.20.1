package com.krampus.legendaryshrines.data;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class ShrineLocations extends SavedData {

    private static final String FILE_NAME = "legendaryshrines_locations";
    private static final String KEY = "Positions";

    private final Set<BlockPos> positions = new HashSet<>();

    public static ShrineLocations get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(ShrineLocations::load, ShrineLocations::new, FILE_NAME);
    }

    public static ShrineLocations load(CompoundTag tag) {
        ShrineLocations data = new ShrineLocations();
        for (long packed : tag.getLongArray(KEY)) {
            data.positions.add(BlockPos.of(packed));
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        long[] packed = new long[this.positions.size()];
        int i = 0;
        for (BlockPos pos : this.positions) {
            packed[i++] = pos.asLong();
        }
        tag.putLongArray(KEY, packed);
        return tag;
    }

    public void add(BlockPos pos) {
        if (this.positions.add(pos.immutable())) {
            setDirty();
        }
    }

    public void remove(BlockPos pos) {
        if (this.positions.remove(pos)) {
            setDirty();
        }
    }

    @Nullable
    public BlockPos findNearest(double x, double y, double z, double radius) {
        double best = radius * radius;
        BlockPos found = null;
        for (BlockPos pos : this.positions) {
            double dx = pos.getX() + 0.5D - x;
            double dy = pos.getY() + 0.5D - y;
            double dz = pos.getZ() + 0.5D - z;
            double distSqr = dx * dx + dy * dy + dz * dz;
            if (distSqr <= best) {
                best = distSqr;
                found = pos;
            }
        }
        return found;
    }
}
