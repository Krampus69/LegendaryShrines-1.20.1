package com.krampus.legendaryshrines.registry;

import com.krampus.legendaryshrines.LegendaryShrines;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, LegendaryShrines.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> SHRINE_ACTIVATED = SOUNDS.register("shrine_activated",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(LegendaryShrines.MOD_ID, "shrine_activated")));

    public static final DeferredHolder<SoundEvent, SoundEvent> RESURECT = SOUNDS.register("resurect",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(LegendaryShrines.MOD_ID, "resurect")));

    public static void register(IEventBus bus) {
        SOUNDS.register(bus);
    }
}
