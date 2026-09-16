package com.krampus.legendaryshrines.registry;

import com.krampus.legendaryshrines.LegendaryShrines;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, LegendaryShrines.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RUNE =
            PARTICLE_TYPES.register("rune", () -> new SimpleParticleType(false) {
            });

    public static void register(IEventBus bus) {
        PARTICLE_TYPES.register(bus);
    }
}
