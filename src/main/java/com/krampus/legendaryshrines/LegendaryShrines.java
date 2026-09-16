package com.krampus.legendaryshrines;

import com.krampus.legendaryshrines.config.ShrineConfig;
import com.krampus.legendaryshrines.network.ModNetwork;
import com.krampus.legendaryshrines.registry.ModBlockEntities;
import com.krampus.legendaryshrines.registry.ModBlocks;
import com.krampus.legendaryshrines.registry.ModCreativeTabs;
import com.krampus.legendaryshrines.registry.ModItems;
import com.krampus.legendaryshrines.registry.ModParticles;
import com.krampus.legendaryshrines.registry.ModSounds;
import com.krampus.legendaryshrines.registry.ModStructures;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(LegendaryShrines.MOD_ID)
public class LegendaryShrines {

    public static final String MOD_ID = "legendaryshrines";

    public LegendaryShrines(IEventBus modBus, ModContainer container) {
        ModBlocks.register(modBus);
        ModItems.register(modBus);
        ModCreativeTabs.register(modBus);
        ModSounds.register(modBus);
        ModBlockEntities.register(modBus);
        ModParticles.register(modBus);
        ModStructures.register(modBus);

        container.registerConfig(ModConfig.Type.SERVER, ShrineConfig.SPEC);

        modBus.addListener(ModNetwork::register);
    }
}
