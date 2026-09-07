package com.krampus.legendaryshrines.registry;

import com.krampus.legendaryshrines.LegendaryShrines;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, LegendaryShrines.MOD_ID);

    public static final RegistryObject<Item> SHRINE = ITEMS.register("shrine",
            () -> new BlockItem(ModBlocks.SHRINE.get(), new Item.Properties()));


    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
