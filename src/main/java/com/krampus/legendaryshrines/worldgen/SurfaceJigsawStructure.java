package com.krampus.legendaryshrines.worldgen;

import com.krampus.legendaryshrines.registry.ModStructures;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.Optional;

public class SurfaceJigsawStructure extends Structure {

    public static final Codec<SurfaceJigsawStructure> CODEC =
            RecordCodecBuilder.<SurfaceJigsawStructure>mapCodec(instance -> instance.group(
                    settingsCodec(instance),
                    StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(s -> s.startPool),
                    Codec.intRange(0, 7).fieldOf("size").forGetter(s -> s.maxDepth),
                    HeightProvider.CODEC.fieldOf("start_height").forGetter(s -> s.startHeight),
                    Heightmap.Types.CODEC.fieldOf("project_start_to_heightmap")
                            .forGetter(s -> s.projectStartToHeightmap),
                    Codec.intRange(1, 128).fieldOf("max_distance_from_center")
                            .forGetter(s -> s.maxDistanceFromCenter)
            ).apply(instance, SurfaceJigsawStructure::new)).codec();

    private final Holder<StructureTemplatePool> startPool;
    private final int maxDepth;
    private final HeightProvider startHeight;
    private final Heightmap.Types projectStartToHeightmap;
    private final int maxDistanceFromCenter;

    public SurfaceJigsawStructure(StructureSettings settings,
                                  Holder<StructureTemplatePool> startPool,
                                  int maxDepth,
                                  HeightProvider startHeight,
                                  Heightmap.Types projectStartToHeightmap,
                                  int maxDistanceFromCenter) {
        super(settings);
        this.startPool = startPool;
        this.maxDepth = maxDepth;
        this.startHeight = startHeight;
        this.projectStartToHeightmap = projectStartToHeightmap;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        int x = chunkPos.getMinBlockX();
        int z = chunkPos.getMinBlockZ();

        int surface = context.chunkGenerator().getFirstFreeHeight(
                x, z, this.projectStartToHeightmap, context.heightAccessor(), context.randomState());

        if (surface <= context.chunkGenerator().getSeaLevel()) {
            return Optional.empty();
        }

        int y = this.startHeight.sample(context.random(),
                new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));

        return JigsawPlacement.addPieces(context, this.startPool, Optional.empty(), this.maxDepth,
                new BlockPos(x, y, z), false, Optional.of(this.projectStartToHeightmap),
                this.maxDistanceFromCenter);
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.SURFACE_JIGSAW.get();
    }
}
