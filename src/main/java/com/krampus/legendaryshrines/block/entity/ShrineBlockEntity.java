package com.krampus.legendaryshrines.block.entity;

import com.krampus.legendaryshrines.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ShrineBlockEntity extends BlockEntity {

    public ShrineBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SHRINE.get(), pos, state);
    }
}
