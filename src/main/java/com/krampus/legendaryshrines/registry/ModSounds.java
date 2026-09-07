package com.krampus.legendaryshrines.registry;

import com.krampus.legendaryshrines.LegendaryShrines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, LegendaryShrines.MOD_ID);

    public static final RegistryObject<SoundEvent> SHRINE_ACTIVATED = SOUNDS.register("shrine_activated",
            () -> SoundEvent.createVariableRangeEvent(
                    new ResourceLocation(LegendaryShrines.MOD_ID, "shrine_activated")));

    public static final RegistryObject<SoundEvent> RESURECT = SOUNDS.register("resurect",
            () -> SoundEvent.createVariableRangeEvent(
                    new ResourceLocation(LegendaryShrines.MOD_ID, "resurect")));

    public static void register(IEventBus bus) {
        SOUNDS.register(bus);
    }
}
