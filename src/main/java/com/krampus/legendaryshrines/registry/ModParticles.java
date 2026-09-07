package com.krampus.legendaryshrines.registry;

import com.krampus.legendaryshrines.LegendaryShrines;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, LegendaryShrines.MOD_ID);

    public static final RegistryObject<SimpleParticleType> RUNE =
            PARTICLE_TYPES.register("rune", () -> new SimpleParticleType(false) {
            });

    public static void register(IEventBus bus) {
        PARTICLE_TYPES.register(bus);
    }
}
