package com.krampus.legendaryshrines.registry;

import com.krampus.legendaryshrines.LegendaryShrines;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LegendaryShrines.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MAIN = TABS.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.legendaryshrines.main"))
                    .icon(() -> new ItemStack(ModItems.SHRINE.get()))
                    .displayItems((parameters, output) -> output.accept(ModItems.SHRINE.get()))
                    .build());


    public static void register(IEventBus bus) {
        TABS.register(bus);
    }
}
