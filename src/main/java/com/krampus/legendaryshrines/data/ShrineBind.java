package com.krampus.legendaryshrines.data;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public record ShrineBind(BlockPos pos, ResourceKey<Level> dimension) {

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.put("Pos", NbtUtils.writeBlockPos(this.pos));
        tag.putString("Dimension", this.dimension.location().toString());
        return tag;
    }

    public static ShrineBind load(CompoundTag tag) {
        BlockPos pos = NbtUtils.readBlockPos(tag.getCompound("Pos"));
        ResourceKey<Level> dimension = ResourceKey.create(
                Registries.DIMENSION,
                new ResourceLocation(tag.getString("Dimension")));
        return new ShrineBind(pos, dimension);
    }

    public double distanceSqrTo(double x, double y, double z) {
        double dx = this.pos.getX() + 0.5D - x;
        double dy = this.pos.getY() + 0.5D - y;
        double dz = this.pos.getZ() + 0.5D - z;
        return dx * dx + dy * dy + dz * dz;
    }
}
