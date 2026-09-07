package com.krampus.legendaryshrines.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.krampus.legendaryshrines.block.entity.ShrineBlockEntity;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.util.RandomSource;

import java.util.List;

public class ShrineBlockRenderer implements BlockEntityRenderer<ShrineBlockEntity> {

    private static final RandomSource RANDOM = RandomSource.create();
    private static final int VIEW_DISTANCE = 128;

    public ShrineBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(ShrineBlockEntity shrine, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Level level = shrine.getLevel();
        if (level == null) {
            return;
        }

        BakedModel runes = ClientModelHandler.runesFor(ClientShrineState.ticksSinceBind(level.getGameTime()));
        if (runes == null) {
            return;
        }

        BlockPos lit = ClientShrineState.litPos();
        if (lit == null || !lit.equals(shrine.getBlockPos())) {
            return;
        }

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.cutout());
        PoseStack.Pose pose = poseStack.last();

        emit(consumer, pose, runes.getQuads(null, null, RANDOM));
        for (Direction direction : Direction.values()) {
            emit(consumer, pose, runes.getQuads(null, direction, RANDOM));
        }
    }

    @Override
    public int getViewDistance() {
        return VIEW_DISTANCE;
    }

    private static void emit(VertexConsumer consumer, PoseStack.Pose pose, List<BakedQuad> quads) {
        for (BakedQuad quad : quads) {
            consumer.putBulkData(pose, quad, 1.0F, 1.0F, 1.0F,
                    LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
        }
    }
}
