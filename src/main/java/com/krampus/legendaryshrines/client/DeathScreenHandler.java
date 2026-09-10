package com.krampus.legendaryshrines.client;

import com.krampus.legendaryshrines.LegendaryShrines;
import com.krampus.legendaryshrines.config.ShrineConfig;
import com.krampus.legendaryshrines.network.ModNetwork;
import com.krampus.legendaryshrines.network.RespawnAtShrinePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = LegendaryShrines.MOD_ID, value = Dist.CLIENT)
public final class DeathScreenHandler {

    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int ACTIVATION_DELAY = 20;

    @Nullable
    private static Button shrineButton;
    private static int delayTicker;

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        shrineButton = null;

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

        Button button = Button.builder(
                        Component.translatable("gui.legendaryshrines.respawn_at_shrine"),
                        pressed -> {
                            Minecraft minecraft = Minecraft.getInstance();
                            ModNetwork.CHANNEL.sendToServer(new RespawnAtShrinePacket());
                            if (minecraft.player != null) {
                                minecraft.player.respawn();
                            }
                            minecraft.setScreen(null);
                        })
                .bounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build();

        button.active = false;
        delayTicker = 0;
        shrineButton = button;
        event.addListener(button);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || shrineButton == null) {
            return;
        }

        if (Minecraft.getInstance().screen == null) {
            shrineButton = null;
            return;
        }

        if (delayTicker < ACTIVATION_DELAY) {
            delayTicker++;
            shrineButton.active = delayTicker >= ACTIVATION_DELAY;
        }
    }
}
