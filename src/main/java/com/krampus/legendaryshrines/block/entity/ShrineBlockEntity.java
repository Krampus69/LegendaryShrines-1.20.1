package com.krampus.legendaryshrines.block.entity;

import com.krampus.legendaryshrines.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class ShrineBlockEntity extends BlockEntity {

    private static final int RENDER_HEIGHT = 3;

    public ShrineBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SHRINE.get(), pos, state);
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(
                this.worldPosition.getX(),
                this.worldPosition.getY(),
                this.worldPosition.getZ(),
                this.worldPosition.getX() + 1,
                this.worldPosition.getY() + RENDER_HEIGHT,
                this.worldPosition.getZ() + 1);
    }
}
