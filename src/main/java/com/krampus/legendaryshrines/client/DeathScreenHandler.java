package com.krampus.legendaryshrines.client;

import com.krampus.legendaryshrines.LegendaryShrines;
import com.krampus.legendaryshrines.config.ShrineConfig;
import com.krampus.legendaryshrines.network.ModNetwork;
import com.krampus.legendaryshrines.network.RespawnAtShrinePacket;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = LegendaryShrines.MOD_ID, value = Dist.CLIENT)
public final class DeathScreenHandler {

    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final long ACTIVATION_DELAY_MS = 1000L;

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        if (!(event.getScreen() instanceof DeathScreen screen)) {
            return;
        }
        if (ShrineConfig.OVERRIDE_VANILLA_RESPAWN.get()) {
            return;
        }
        if (!ClientShrineState.canRespawnAtShrine()) {
            return;
        }

        int x = screen.width / 2 - BUTTON_WIDTH / 2;
        int y = screen.height / 4 + 48;
        long created = Util.getMillis();
        Button vanillaButton = null;
        for (GuiEventListener child : screen.children()) {
            if (child instanceof Button existing) {
                vanillaButton = existing;
                break;
            }
        }
        Button reference = vanillaButton;

        Button button = new Button(x, y, BUTTON_WIDTH, BUTTON_HEIGHT,
                Component.translatable("gui.legendaryshrines.respawn_at_shrine"),
                pressed -> {
                    Minecraft minecraft = Minecraft.getInstance();
                    ModNetwork.CHANNEL.sendToServer(new RespawnAtShrinePacket());
                    if (minecraft.player != null) {
                        minecraft.player.respawn();
                    }
                    minecraft.setScreen(null);
                },
                Supplier::get) {
            @Override
            protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
                this.active = reference != null
                        ? reference.active
                        : Util.getMillis() - created >= ACTIVATION_DELAY_MS;
                super.renderWidget(graphics, mouseX, mouseY, partialTick);
            }
        };

        button.active = false;
        event.addListener(button);
    }
}