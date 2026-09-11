package com.yourname.backrooms.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yourname.backrooms.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class BackroomsChunkGenerator extends ChunkGenerator {
    public static final Codec<BackroomsChunkGenerator> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BiomeSource.CODEC.fieldOf("biome_source").forGetter(BackroomsChunkGenerator::getBiomeSource)
            ).apply(instance, instance.stable(BackroomsChunkGenerator::new))
    );

    public BackroomsChunkGenerator(BiomeSource biomeSource) {
        super(biomeSource);
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public void applyCarvers(WorldGenRegion region, long seed, RandomState randomState, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunk, GenerationStep.Carving step) {
    }

    @Override
    public void buildSurface(WorldGenRegion region, StructureManager structureManager, RandomState randomState, ChunkAccess chunk) {
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion region) {
    }

    @Override
    public int getMinY() {
        return 0;
    }

    @Override
    public int getGenDepth() {
        return 256;
    }

    @Override
    public int getSeaLevel() {
        return 64;
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor level, RandomState randomState) {
        return 68;
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor level, RandomState randomState) {
        return new NoiseColumn(0, new BlockState[0]);
    }

    @Override
    public void addDebugScreenInfo(List<String> list, RandomState randomState, BlockPos pos) {
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunk) {
        return CompletableFuture.supplyAsync(() -> {
            int chunkX = chunk.getPos().x;
            int chunkZ = chunk.getPos().z;

            BlockState bedrock = Blocks.BEDROCK.defaultBlockState();
            BlockState frame = ModBlocks.BACKROOMS_FRAME.get().defaultBlockState();
            BlockState carpet = ModBlocks.MOIST_CARPET.get().defaultBlockState();
            BlockState wallpaper = ModBlocks.YELLOW_WALLPAPER.get().defaultBlockState();
            BlockState baseboard = ModBlocks.WALLPAPER_BASEBOARD.get().defaultBlockState();
            BlockState ceiling = ModBlocks.CEILING_TILE.get().defaultBlockState();
            BlockState light = ModBlocks.FLUORESCENT_LIGHT.get().defaultBlockState();
            BlockState air = Blocks.AIR.defaultBlockState();

            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

            for (int x = 0; x < 16; x++) {
                int worldX = chunkX * 16 + x;
                for (int z = 0; z < 16; z++) {
                    int worldZ = chunkZ * 16 + z;

                    // Bottom bedrock & subfloor
                    chunk.setBlockState(pos.set(x, 62, z), bedrock, false);
                    chunk.setBlockState(pos.set(x, 63, z), frame, false);

                    // Floor (Y=64)
                    chunk.setBlockState(pos.set(x, 64, z), carpet, false);

                    // Room walls & air space (Y=65 to 67)
                    boolean isWall = isBackroomsWall(worldX, worldZ);
                    for (int y = 65; y <= 67; y++) {
                        if (isWall) {
                            if (y == 65) {
                                chunk.setBlockState(pos.set(x, y, z), baseboard, false);
                            } else {
                                chunk.setBlockState(pos.set(x, y, z), wallpaper, false);
                            }
                        } else {
                            chunk.setBlockState(pos.set(x, y, z), air, false);
                        }
                    }

                    // Ceiling (Y=68)
                    if ((worldX % 5 == 0 && worldZ % 5 == 0) && !isWall) {
                        chunk.setBlockState(pos.set(x, 68, z), light, false);
                    } else {
                        chunk.setBlockState(pos.set(x, 68, z), ceiling, false);
                    }

                    // Top ceiling frame & bedrock
                    chunk.setBlockState(pos.set(x, 69, z), frame, false);
                    chunk.setBlockState(pos.set(x, 70, z), bedrock, false);
                }
            }
            return chunk;
        }, executor);
    }

    private static boolean isBackroomsWall(int x, int z) {
        int cellX = Math.floorDiv(x, 7);
        int cellZ = Math.floorDiv(z, 7);
        int modX = Math.floorMod(x, 7);
        int modZ = Math.floorMod(z, 7);

        // Border of 7x7 cells form potential walls
        if (modX == 0 || modZ == 0) {
            // Pseudo random doorway opening check
            long hash = (cellX * 3129871L) ^ (cellZ * 116129781L) ^ (modX * 17L) ^ (modZ * 23L);
            long doorHash = (cellX * 73856093L) ^ (cellZ * 19349663L);
            int doorPos = (int) Math.abs(doorHash % 5) + 1;

            if (modX == 0 && (modZ == doorPos || modZ == doorPos + 1)) {
                return false; // Doorway in X wall
            }
            if (modZ == 0 && (modX == doorPos || modX == doorPos + 1)) {
                return false; // Doorway in Z wall
            }

            // Pillars or wall segments
            return Math.abs(hash % 4) != 0;
        }

        // Random central pillar in some cells
        long roomHash = (cellX * 15485863L) ^ (cellZ * 32452843L);
        if (Math.abs(roomHash % 3) == 0) {
            return (modX == 3 || modX == 4) && (modZ == 3 || modZ == 4);
        }

        return false;
    }
}
