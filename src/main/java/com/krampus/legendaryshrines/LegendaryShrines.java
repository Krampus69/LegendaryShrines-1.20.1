package com.krampus.legendaryshrines;

import com.krampus.legendaryshrines.network.ModNetwork;
import com.krampus.legendaryshrines.config.ShrineConfig;
import com.krampus.legendaryshrines.registry.ModBlockEntities;
import com.krampus.legendaryshrines.registry.ModBlocks;
import com.krampus.legendaryshrines.registry.ModCreativeTabs;
import com.krampus.legendaryshrines.registry.ModItems;
import com.krampus.legendaryshrines.registry.ModParticles;
import com.krampus.legendaryshrines.registry.ModSounds;
import com.krampus.legendaryshrines.registry.ModStructures;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(LegendaryShrines.MOD_ID)
public class LegendaryShrines {

    public static final String MOD_ID = "legendaryshrines";

    public LegendaryShrines() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlocks.register(modBus);
        ModItems.register(modBus);
        ModCreativeTabs.register(modBus);
        ModSounds.register(modBus);
        ModBlockEntities.register(modBus);
        ModParticles.register(modBus);
        ModStructures.register(modBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ShrineConfig.SPEC);

        modBus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ModNetwork::register);
    }
}
