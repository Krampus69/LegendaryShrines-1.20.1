package com.krampus.legendaryshrines.client;

import com.krampus.legendaryshrines.LegendaryShrines;
import com.krampus.legendaryshrines.client.particle.RuneParticle;
import com.krampus.legendaryshrines.registry.ModBlockEntities;
import com.krampus.legendaryshrines.registry.ModParticles;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = LegendaryShrines.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ClientModelHandler {

    public static final ResourceLocation RUNES_MODEL =
            new ResourceLocation(LegendaryShrines.MOD_ID, "block/shrine_runes");

    private static final int ACTIVATION_FRAMES = 8;
    private static final int FRAME_TICKS = 2;

    private static final ResourceLocation[] ACTIVATED_MODELS = new ResourceLocation[ACTIVATION_FRAMES];
    private static final BakedModel[] ACTIVATED = new BakedModel[ACTIVATION_FRAMES];

    static {
        for (int i = 0; i < ACTIVATION_FRAMES; i++) {
            ACTIVATED_MODELS[i] = new ResourceLocation(
                    LegendaryShrines.MOD_ID, "block/shrine_runes_activated_" + i);
        }
    }

    @Nullable
    private static BakedModel runes;

    @Nullable
    public static BakedModel runesFor(long ticksSinceBind) {
        if (ticksSinceBind < 0L) {
            return runes;
        }

        int frame = (int) Math.min(ticksSinceBind / FRAME_TICKS, ACTIVATION_FRAMES);
        if (frame >= ACTIVATION_FRAMES || ACTIVATED[frame] == null) {
            return runes;
        }
        return ACTIVATED[frame];
    }

    @SubscribeEvent
    public static void onRegisterAdditional(ModelEvent.RegisterAdditional event) {
        event.register(RUNES_MODEL);
        for (ResourceLocation model : ACTIVATED_MODELS) {
            event.register(model);
        }
    }

    @SubscribeEvent
    public static void onBakingCompleted(ModelEvent.BakingCompleted event) {
        runes = event.getModels().get(RUNES_MODEL);
        for (int i = 0; i < ACTIVATION_FRAMES; i++) {
            ACTIVATED[i] = event.getModels().get(ACTIVATED_MODELS[i]);
        }
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.SHRINE.get(), ShrineBlockRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.RUNE.get(), RuneParticle.Provider::new);
    }
}
