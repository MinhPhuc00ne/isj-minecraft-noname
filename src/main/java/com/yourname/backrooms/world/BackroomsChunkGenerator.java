package com.yourname.backrooms.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yourname.backrooms.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
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
            BlockState ghostCarpet = ModBlocks.GHOST_MOIST_CARPET.get().defaultBlockState();
            BlockState wallpaper = ModBlocks.YELLOW_WALLPAPER.get().defaultBlockState();
            BlockState mossyWallpaper = ModBlocks.MOSSY_WALLPAPER.get().defaultBlockState();
            BlockState baseboard = ModBlocks.WALLPAPER_BASEBOARD.get().defaultBlockState();
            BlockState ceiling = ModBlocks.CEILING_TILE.get().defaultBlockState();
            BlockState light = ModBlocks.FLUORESCENT_LIGHT.get().defaultBlockState();
            BlockState flickeringLight = ModBlocks.FLICKERING_LIGHT.get().defaultBlockState();
            BlockState darkEarth = ModBlocks.DARK_MOIST_EARTH.get().defaultBlockState();
            BlockState chest = Blocks.CHEST.defaultBlockState();
            BlockState cassettePlayer = ModBlocks.CASSETTE_PLAYER.get().defaultBlockState();
            BlockState air = Blocks.AIR.defaultBlockState();
            BlockState oakLeaves = Blocks.OAK_LEAVES.defaultBlockState();
            BlockState shortGrass = Blocks.GRASS.defaultBlockState();

            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

            for (int x = 0; x < 16; x++) {
                int worldX = chunkX * 16 + x;
                for (int z = 0; z < 16; z++) {
                    int worldZ = chunkZ * 16 + z;

                    int cellX = Math.floorDiv(worldX, 7);
                    int cellZ = Math.floorDiv(worldZ, 7);
                    long roomTypeHash = Math.abs((cellX * 73856093L) ^ (cellZ * 19349663L));
                    int roomType = (int) (roomTypeHash % 7); // 0: Forest, 1: Trap, 2: Chest/Storage, 3-4: Large Hall, 5-6: Monoyellow

                    // Subfloor & bedrock
                    chunk.setBlockState(pos.set(x, 60, z), bedrock, false);
                    chunk.setBlockState(pos.set(x, 61, z), frame, false);

                    // Floor & Traps (Y=64)
                    if (roomType == 1 && ((worldX % 7 == 3 && worldZ % 7 == 3) || (worldX % 7 == 4 && worldZ % 7 == 4))) {
                        // Ghost carpet trap over hollow hole
                        chunk.setBlockState(pos.set(x, 62, z), air, false);
                        chunk.setBlockState(pos.set(x, 63, z), air, false);
                        chunk.setBlockState(pos.set(x, 64, z), ghostCarpet, false);
                    } else if (roomType == 0) {
                        // Dark Indoor Forest floor
                        chunk.setBlockState(pos.set(x, 62, z), frame, false);
                        chunk.setBlockState(pos.set(x, 63, z), darkEarth, false);
                        chunk.setBlockState(pos.set(x, 64, z), darkEarth, false);
                    } else {
                        chunk.setBlockState(pos.set(x, 62, z), frame, false);
                        chunk.setBlockState(pos.set(x, 63, z), frame, false);
                        chunk.setBlockState(pos.set(x, 64, z), carpet, false);
                    }

                    // Walls & Air (Y=65 to 67)
                    boolean isWall = isBackroomsWall(worldX, worldZ, roomType);
                    BlockState currentWall = (roomType == 0) ? mossyWallpaper : wallpaper;

                    for (int y = 65; y <= 67; y++) {
                        if (isWall) {
                            if (y == 65) {
                                chunk.setBlockState(pos.set(x, y, z), (roomType == 0) ? mossyWallpaper : baseboard, false);
                            } else {
                                chunk.setBlockState(pos.set(x, y, z), currentWall, false);
                            }
                        } else {
                            chunk.setBlockState(pos.set(x, y, z), air, false);
                        }
                    }

                    // Forest room vegetation
                    if (roomType == 0 && !isWall && (worldX % 7 != 0 && worldZ % 7 != 0)) {
                        if ((worldX % 7 == 2 && worldZ % 7 == 2)) {
                            chunk.setBlockState(pos.set(x, 65, z), oakLeaves, false);
                            chunk.setBlockState(pos.set(x, 66, z), oakLeaves, false);
                        } else if ((worldX + worldZ) % 5 == 0) {
                            chunk.setBlockState(pos.set(x, 65, z), shortGrass, false);
                        }
                    }

                    // Chest & Cassette Player placement in Storage Rooms
                    if (roomType == 2 && worldX % 7 == 3 && worldZ % 7 == 3) {
                        chunk.setBlockState(pos.set(x, 65, z), chest, false);
                    } else if (roomType == 2 && worldX % 7 == 4 && worldZ % 7 == 4) {
                        chunk.setBlockState(pos.set(x, 65, z), cassettePlayer, false);
                    }

                    // Ceiling (Y=68)
                    if ((worldX % 5 == 0 && worldZ % 5 == 0) && !isWall) {
                        if (roomType == 1) {
                            chunk.setBlockState(pos.set(x, 68, z), flickeringLight, false);
                        } else {
                            chunk.setBlockState(pos.set(x, 68, z), light, false);
                        }
                    } else {
                        chunk.setBlockState(pos.set(x, 68, z), ceiling, false);
                    }

                    chunk.setBlockState(pos.set(x, 69, z), frame, false);
                    chunk.setBlockState(pos.set(x, 70, z), bedrock, false);
                }
            }

            // Doorway Door Placement
            placeDoorsInChunk(chunk, chunkX, chunkZ);

            return chunk;
        }, executor);
    }

    private static boolean isBackroomsWall(int x, int z, int roomType) {
        int cellX = Math.floorDiv(x, 7);
        int cellZ = Math.floorDiv(z, 7);
        int modX = Math.floorMod(x, 7);
        int modZ = Math.floorMod(z, 7);

        // Large Halls (Type 3 & 4): remove internal walls between 2x2 cells
        if ((roomType == 3 || roomType == 4)) {
            if ((cellX % 2 == 0 && modX == 0) || (cellZ % 2 == 0 && modZ == 0)) {
                if (modX == 3 || modX == 4 || modZ == 3 || modZ == 4) return false;
            }
        }

        if (modX == 0 || modZ == 0) {
            long doorHash = (cellX * 73856093L) ^ (cellZ * 19349663L);
            int doorPos = (int) Math.abs(doorHash % 4) + 2;

            if (modX == 0 && (modZ == doorPos || modZ == doorPos + 1)) return false;
            if (modZ == 0 && (modX == doorPos || modX == doorPos + 1)) return false;

            return true;
        }

        // Central pillar in large halls
        if (roomType == 3 || roomType == 4) {
            return (modX == 3 && modZ == 3);
        }

        return false;
    }

    private static void placeDoorsInChunk(ChunkAccess chunk, int chunkX, int chunkZ) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int x = 0; x < 16; x++) {
            int worldX = chunkX * 16 + x;
            for (int z = 0; z < 16; z++) {
                int worldZ = chunkZ * 16 + z;
                int modX = Math.floorMod(worldX, 7);
                int modZ = Math.floorMod(worldZ, 7);

                if (modX == 0 && (modZ == 2 || modZ == 3)) {
                    int cellX = Math.floorDiv(worldX, 7);
                    int cellZ = Math.floorDiv(worldZ, 7);
                    long hash = Math.abs((cellX * 17L) ^ (cellZ * 31L));

                    if (hash % 3 == 0) {
                        Block doorBlock = ModBlocks.YELLOW_WOOD_DOOR.get();
                        if (hash % 4 == 0) doorBlock = ModBlocks.OFFICE_GLASS_DOOR.get();
                        else if (hash % 4 == 1) doorBlock = ModBlocks.VENT_METAL_DOOR.get();
                        else if (hash % 4 == 2) doorBlock = ModBlocks.MOSSY_FOREST_DOOR.get();

                        net.minecraft.world.level.block.state.properties.DoorHingeSide hinge =
                                (modZ == 2) ? net.minecraft.world.level.block.state.properties.DoorHingeSide.LEFT :
                                        net.minecraft.world.level.block.state.properties.DoorHingeSide.RIGHT;

                        BlockState doorLower = doorBlock.defaultBlockState()
                                .setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER)
                                .setValue(DoorBlock.FACING, net.minecraft.core.Direction.EAST)
                                .setValue(DoorBlock.HINGE, hinge);

                        BlockState doorUpper = doorBlock.defaultBlockState()
                                .setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER)
                                .setValue(DoorBlock.FACING, net.minecraft.core.Direction.EAST)
                                .setValue(DoorBlock.HINGE, hinge);

                        chunk.setBlockState(pos.set(x, 65, z), doorLower, false);
                        chunk.setBlockState(pos.set(x, 66, z), doorUpper, false);
                    }
                }
            }
        }
    }
}
