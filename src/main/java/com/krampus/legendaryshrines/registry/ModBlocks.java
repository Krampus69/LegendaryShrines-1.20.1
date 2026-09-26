package com.krampus.legendaryshrines.registry;

import com.krampus.legendaryshrines.LegendaryShrines;
import com.krampus.legendaryshrines.block.ShrineBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, LegendaryShrines.MOD_ID);

    public static final RegistryObject<ShrineBlock> SHRINE = BLOCKS.register("shrine",
            () -> new ShrineBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .sound(SoundType.DEEPSLATE)
                    .strength(5.0F, 1200.0F)
                    .pushReaction(PushReaction.BLOCK)
                    .noLootTable()
                    .noOcclusion()));


    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
