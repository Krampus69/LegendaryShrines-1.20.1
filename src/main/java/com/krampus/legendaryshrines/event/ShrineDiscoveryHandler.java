package com.krampus.legendaryshrines.event;

import com.krampus.legendaryshrines.LegendaryShrines;
import com.krampus.legendaryshrines.block.ShrineBlock;
import com.krampus.legendaryshrines.data.ShrineLocations;
import com.krampus.legendaryshrines.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LegendaryShrines.MOD_ID)
public final class ShrineDiscoveryHandler {

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }

        ChunkAccess chunk = event.getChunk();
        ShrineLocations locations = ShrineLocations.get(level);

        chunk.findBlocks(ShrineDiscoveryHandler::isLowerShrine, (pos, state) -> {
            BlockPos shrine = pos.immutable();
            locations.add(shrine);
            ensureBlockEntity(chunk, shrine);
        });
    }

    private static void ensureBlockEntity(ChunkAccess chunk, BlockPos pos) {
        if (!(chunk instanceof LevelChunk levelChunk)) {
            return;
        }
        if (levelChunk.getBlockEntity(pos, LevelChunk.EntityCreationType.CHECK) != null) {
            return;
        }
        if (levelChunk.getBlockEntity(pos, LevelChunk.EntityCreationType.IMMEDIATE) != null) {
            levelChunk.setUnsaved(true);
        }
    }

    private static boolean isLowerShrine(BlockState state) {
        return state.is(ModBlocks.SHRINE.get())
                && state.getValue(ShrineBlock.HALF) == DoubleBlockHalf.LOWER;
    }
}
