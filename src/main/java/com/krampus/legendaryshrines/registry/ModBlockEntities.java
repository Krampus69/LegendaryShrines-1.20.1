package com.krampus.legendaryshrines.registry;

import com.krampus.legendaryshrines.LegendaryShrines;
import com.krampus.legendaryshrines.block.entity.ShrineBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, LegendaryShrines.MOD_ID);

    public static final RegistryObject<BlockEntityType<ShrineBlockEntity>> SHRINE =
            BLOCK_ENTITIES.register("shrine",
                    () -> BlockEntityType.Builder.of(ShrineBlockEntity::new, ModBlocks.SHRINE.get()).build(null));

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}
