package com.krampus.legendaryshrines.registry;

import com.krampus.legendaryshrines.LegendaryShrines;
import com.krampus.legendaryshrines.worldgen.SurfaceJigsawStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModStructures {

    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES =
            DeferredRegister.create(Registries.STRUCTURE_TYPE, LegendaryShrines.MOD_ID);

    public static final RegistryObject<StructureType<SurfaceJigsawStructure>> SURFACE_JIGSAW =
            STRUCTURE_TYPES.register("surface_jigsaw", () -> () -> SurfaceJigsawStructure.CODEC);

    public static void register(IEventBus bus) {
        STRUCTURE_TYPES.register(bus);
    }
}
