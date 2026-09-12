package com.yourname.backrooms.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yourname.backrooms.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
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

    public static final int CELL_SIZE = 15;

    // Room Types
    public static final int TYPE_WIDE_GREAT_HALL = 0;
    public static final int TYPE_DARK_FOREST = 1;
    public static final int TYPE_ABANDONED_VILLAGE = 2;
    public static final int TYPE_TRAP_ROOM = 3;
    public static final int TYPE_LONG_GRAND_CORRIDOR_X = 4;
    public static final int TYPE_LONG_GRAND_CORRIDOR_Z = 5;
    public static final int TYPE_NARROW_CORRIDOR = 6;
    public static final int TYPE_STANDARD_MONOYELLOW = 7; // 7..11 standard monoyellow rooms

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

    public static int getRoomType(int cellX, int cellZ) {
        long hash = Math.abs((cellX * 73856093L) ^ (cellZ * 19349663L));
        int val = (int) (hash % 12);
        if (val >= 7) return TYPE_STANDARD_MONOYELLOW;
        return val;
    }

    public static boolean isClosedRoom(int cellX, int cellZ) {
        int type = getRoomType(cellX, cellZ);
        return type == TYPE_STANDARD_MONOYELLOW || type == TYPE_TRAP_ROOM || type == TYPE_NARROW_CORRIDOR;
    }

    /**
     * Determines which side of the room (0: West, 1: East, 2: North, 3: South) contains the single entrance door.
     */
    public static int getSingleDoorSide(int cellX, int cellZ) {
        long hash = Math.abs((cellX * 73856093L) ^ (cellZ * 19349663L));
        return (int) (hash % 4);
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunk) {
        return CompletableFuture.supplyAsync(() -> {
            int chunkX = chunk.getPos().x;
            int chunkZ = chunk.getPos().z;

            // Block States
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
            BlockState signBlock = ModBlocks.BACKROOMS_SIGN.get().defaultBlockState();
            BlockState cobweb = Blocks.COBWEB.defaultBlockState();
            BlockState chest = Blocks.CHEST.defaultBlockState();
            BlockState cassettePlayer = ModBlocks.CASSETTE_PLAYER.get().defaultBlockState();
            BlockState air = Blocks.AIR.defaultBlockState();
            BlockState oakLeaves = Blocks.OAK_LEAVES.defaultBlockState();
            BlockState oakLog = Blocks.OAK_LOG.defaultBlockState();
            BlockState spruceLeaves = Blocks.SPRUCE_LEAVES.defaultBlockState();
            BlockState spruceLog = Blocks.SPRUCE_LOG.defaultBlockState();
            BlockState shortGrass = Blocks.GRASS.defaultBlockState();
            BlockState redMushroom = Blocks.RED_MUSHROOM.defaultBlockState();
            BlockState brownMushroom = Blocks.BROWN_MUSHROOM.defaultBlockState();

            // Village House blocks
            BlockState cobble = Blocks.COBBLESTONE.defaultBlockState();
            BlockState oakPlanks = Blocks.OAK_PLANKS.defaultBlockState();
            BlockState glassPane = Blocks.GLASS_PANE.defaultBlockState();
            BlockState craftingTable = Blocks.CRAFTING_TABLE.defaultBlockState();
            BlockState furnace = Blocks.FURNACE.defaultBlockState();
            BlockState oakFence = Blocks.OAK_FENCE.defaultBlockState();
            BlockState bookshelf = Blocks.BOOKSHELF.defaultBlockState();
            BlockState hayBlock = Blocks.HAY_BLOCK.defaultBlockState();

            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

            for (int x = 0; x < 16; x++) {
                int worldX = chunkX * 16 + x;
                for (int z = 0; z < 16; z++) {
                    int worldZ = chunkZ * 16 + z;

                    int cellX = Math.floorDiv(worldX, CELL_SIZE);
                    int cellZ = Math.floorDiv(worldZ, CELL_SIZE);
                    int modX = Math.floorMod(worldX, CELL_SIZE);
                    int modZ = Math.floorMod(worldZ, CELL_SIZE);
                    int roomType = getRoomType(cellX, cellZ);

                    // Subfloor bedrock
                    chunk.setBlockState(pos.set(x, 58, z), bedrock, false);
                    chunk.setBlockState(pos.set(x, 59, z), frame, false);
                    chunk.setBlockState(pos.set(x, 62, z), frame, false);
                    chunk.setBlockState(pos.set(x, 63, z), frame, false);

                    // ==========================================
                    // 1. MASSIVE DARK INDOOR FOREST (Height 15, Y=65..79, Ceiling Y=80)
                    // ==========================================
                    if (roomType == TYPE_DARK_FOREST) {
                        chunk.setBlockState(pos.set(x, 63, z), darkEarth, false);
                        chunk.setBlockState(pos.set(x, 64, z), darkEarth, false);

                        boolean isForestBorder = (modX == 0 && getRoomType(cellX - 1, cellZ) != TYPE_DARK_FOREST) ||
                                (modX == 14 && getRoomType(cellX + 1, cellZ) != TYPE_DARK_FOREST) ||
                                (modZ == 0 && getRoomType(cellX, cellZ - 1) != TYPE_DARK_FOREST) ||
                                (modZ == 14 && getRoomType(cellX, cellZ + 1) != TYPE_DARK_FOREST);

                        for (int y = 65; y <= 79; y++) {
                            if (isForestBorder) {
                                chunk.setBlockState(pos.set(x, y, z), mossyWallpaper, false);
                            } else {
                                chunk.setBlockState(pos.set(x, y, z), air, false);
                            }
                        }
                        chunk.setBlockState(pos.set(x, 80, z), ceiling, false);
                        chunk.setBlockState(pos.set(x, 81, z), bedrock, false);

                        // Dense Real Forest Vegetation & Trees
                        if (!isForestBorder) {
                            // Main Central Oak Tree
                            if (modX == 7 && modZ == 7) {
                                for (int ty = 65; ty <= 72; ty++) {
                                    chunk.setBlockState(pos.set(x, ty, z), oakLog, false);
                                }
                                for (int lx = -3; lx <= 3; lx++) {
                                    for (int lz = -3; lz <= 3; lz++) {
                                        for (int ly = 71; ly <= 75; ly++) {
                                            if (Math.abs(lx) + Math.abs(lz) <= 4) {
                                                int leafX = x + lx;
                                                int leafZ = z + lz;
                                                if (leafX >= 0 && leafX < 16 && leafZ >= 0 && leafZ < 16) {
                                                    if (chunk.getBlockState(pos.set(leafX, ly, leafZ)).isAir()) {
                                                        chunk.setBlockState(pos.set(leafX, ly, leafZ), oakLeaves, false);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            // Secondary Tall Spruce Tree
                            else if (modX == 3 && modZ == 11) {
                                for (int ty = 65; ty <= 74; ty++) {
                                    chunk.setBlockState(pos.set(x, ty, z), spruceLog, false);
                                }
                                for (int ly = 68; ly <= 76; ly++) {
                                    if (ly % 2 == 0) {
                                        chunk.setBlockState(pos.set(x, ly, z), spruceLeaves, false);
                                    }
                                }
                            }
                            // Tertiary Oak Tree
                            else if (modX == 12 && modZ == 4) {
                                for (int ty = 65; ty <= 71; ty++) {
                                    chunk.setBlockState(pos.set(x, ty, z), oakLog, false);
                                }
                                for (int ly = 70; ly <= 73; ly++) {
                                    chunk.setBlockState(pos.set(x, ly, z), oakLeaves, false);
                                }
                            }
                            // Ground Plants & Mushrooms
                            else if ((worldX * 31 + worldZ * 17) % 5 == 0) {
                                chunk.setBlockState(pos.set(x, 65, z), shortGrass, false);
                            } else if ((worldX * 13 + worldZ * 29) % 17 == 0) {
                                chunk.setBlockState(pos.set(x, 65, z), redMushroom, false);
                            } else if ((worldX * 23 + worldZ * 11) % 19 == 0) {
                                chunk.setBlockState(pos.set(x, 65, z), brownMushroom, false);
                            }
                        }
                        continue;
                    }

                    // ==========================================
                    // 2. LONG GRAND CORRIDORS (Height 8, Y=65..72, Ceiling Y=73)
                    // ==========================================
                    if (roomType == TYPE_LONG_GRAND_CORRIDOR_X || roomType == TYPE_LONG_GRAND_CORRIDOR_Z) {
                        chunk.setBlockState(pos.set(x, 64, z), carpet, false);

                        boolean isCorridorWall = false;
                        if (roomType == TYPE_LONG_GRAND_CORRIDOR_X) {
                            if (modZ == 0 && getRoomType(cellX, cellZ - 1) != TYPE_LONG_GRAND_CORRIDOR_X) isCorridorWall = true;
                            if (modZ == 14 && getRoomType(cellX, cellZ + 1) != TYPE_LONG_GRAND_CORRIDOR_X) isCorridorWall = true;
                        } else {
                            if (modX == 0 && getRoomType(cellX - 1, cellZ) != TYPE_LONG_GRAND_CORRIDOR_Z) isCorridorWall = true;
                            if (modX == 14 && getRoomType(cellX + 1, cellZ) != TYPE_LONG_GRAND_CORRIDOR_Z) isCorridorWall = true;
                        }

                        for (int y = 65; y <= 72; y++) {
                            if (isCorridorWall) {
                                chunk.setBlockState(pos.set(x, y, z), (y == 65) ? baseboard : wallpaper, false);
                            } else {
                                chunk.setBlockState(pos.set(x, y, z), air, false);
                            }
                        }

                        // Fluorescent light strips along center ceiling
                        if (!isCorridorWall && ((roomType == TYPE_LONG_GRAND_CORRIDOR_X && (modZ == 6 || modZ == 8)) || (roomType == TYPE_LONG_GRAND_CORRIDOR_Z && (modX == 6 || modX == 8)))) {
                            chunk.setBlockState(pos.set(x, 73, z), (x % 4 == 0) ? flickeringLight : light, false);
                        } else {
                            chunk.setBlockState(pos.set(x, 73, z), ceiling, false);
                        }
                        chunk.setBlockState(pos.set(x, 74, z), bedrock, false);
                        continue;
                    }

                    // ==========================================
                    // 3. WIDE GREAT HALL (Height 10, Y=65..74, Ceiling Y=75)
                    // ==========================================
                    if (roomType == TYPE_WIDE_GREAT_HALL) {
                        chunk.setBlockState(pos.set(x, 64, z), carpet, false);

                        boolean isHallWall = (modX == 0 && getRoomType(cellX - 1, cellZ) != TYPE_WIDE_GREAT_HALL) ||
                                (modX == 14 && getRoomType(cellX + 1, cellZ) != TYPE_WIDE_GREAT_HALL) ||
                                (modZ == 0 && getRoomType(cellX, cellZ - 1) != TYPE_WIDE_GREAT_HALL) ||
                                (modZ == 14 && getRoomType(cellX, cellZ + 1) != TYPE_WIDE_GREAT_HALL);

                        boolean isPillar = (modX == 4 || modX == 10) && (modZ == 4 || modZ == 10);

                        for (int y = 65; y <= 74; y++) {
                            if (isHallWall) {
                                chunk.setBlockState(pos.set(x, y, z), (y == 65) ? baseboard : wallpaper, false);
                            } else if (isPillar) {
                                chunk.setBlockState(pos.set(x, y, z), wallpaper, false);
                            } else {
                                chunk.setBlockState(pos.set(x, y, z), air, false);
                            }
                        }

                        // Light grid on ceiling
                        if ((modX % 4 == 2) && (modZ % 4 == 2)) {
                            chunk.setBlockState(pos.set(x, 75, z), light, false);
                        } else {
                            chunk.setBlockState(pos.set(x, 75, z), ceiling, false);
                        }
                        chunk.setBlockState(pos.set(x, 76, z), bedrock, false);
                        continue;
                    }

                    // ==========================================
                    // 4. ABANDONED VILLAGE (1 to 5 Distinct Houses)
                    // ==========================================
                    if (roomType == TYPE_ABANDONED_VILLAGE) {
                        chunk.setBlockState(pos.set(x, 64, z), carpet, false);

                        boolean isCellWall = (modX == 0 || modX == 14 || modZ == 0 || modZ == 14);
                        for (int y = 65; y <= 72; y++) {
                            if (isCellWall) {
                                chunk.setBlockState(pos.set(x, y, z), (y == 65) ? baseboard : wallpaper, false);
                            } else {
                                chunk.setBlockState(pos.set(x, y, z), air, false);
                            }
                        }
                        chunk.setBlockState(pos.set(x, 73, z), ceiling, false);
                        chunk.setBlockState(pos.set(x, 74, z), bedrock, false);

                        // Determine number of houses in this village cell (1 to 5)
                        long vHash = Math.abs((cellX * 31L) ^ (cellZ * 17L));
                        int numHouses = 1 + (int) (vHash % 5);

                        // --- HOUSE 1: Peaked Cottage (modX 2..6, modZ 2..6) ---
                        if (numHouses >= 1 && modX >= 2 && modX <= 6 && modZ >= 2 && modZ <= 6) {
                            chunk.setBlockState(pos.set(x, 64, z), cobble, false);
                            if ((modX == 2 || modX == 6) && (modZ == 2 || modZ == 6)) {
                                for (int hy = 65; hy <= 67; hy++) chunk.setBlockState(pos.set(x, hy, z), oakLog, false);
                            } else if (modX == 2 || modX == 6 || modZ == 2 || modZ == 6) {
                                if (modZ == 2 && modX == 4) {
                                    chunk.setBlockState(pos.set(x, 65, z), air, false);
                                    chunk.setBlockState(pos.set(x, 66, z), air, false);
                                } else if ((modX == 2 || modX == 6) && modZ == 4) {
                                    chunk.setBlockState(pos.set(x, 65, z), oakPlanks, false);
                                    chunk.setBlockState(pos.set(x, 66, z), glassPane, false);
                                } else {
                                    chunk.setBlockState(pos.set(x, 65, z), oakPlanks, false);
                                    chunk.setBlockState(pos.set(x, 66, z), oakPlanks, false);
                                }
                                chunk.setBlockState(pos.set(x, 67, z), oakPlanks, false);
                            }
                            chunk.setBlockState(pos.set(x, 68, z), Blocks.OAK_STAIRS.defaultBlockState(), false);
                            if (modX == 4 && modZ == 4) chunk.setBlockState(pos.set(x, 65, z), chest, false);
                        }

                        // --- HOUSE 2: L-Shaped Porch House (modX 8..13, modZ 2..7) ---
                        if (numHouses >= 2 && modX >= 8 && modX <= 13 && modZ >= 2 && modZ <= 7) {
                            chunk.setBlockState(pos.set(x, 64, z), cobble, false);
                            if (modX >= 8 && modX <= 10 && modZ >= 2 && modZ <= 4) {
                                // Front Porch with wooden fences
                                if (modX == 8 || modZ == 2) chunk.setBlockState(pos.set(x, 65, z), oakFence, false);
                            } else if (modX == 8 || modX == 13 || modZ == 2 || modZ == 7) {
                                chunk.setBlockState(pos.set(x, 65, z), oakPlanks, false);
                                chunk.setBlockState(pos.set(x, 66, z), oakPlanks, false);
                                chunk.setBlockState(pos.set(x, 67, z), oakPlanks, false);
                            }
                            chunk.setBlockState(pos.set(x, 68, z), Blocks.COBBLESTONE_STAIRS.defaultBlockState(), false);
                            if (modX == 11 && modZ == 5) chunk.setBlockState(pos.set(x, 65, z), chest, false);
                        }

                        // --- HOUSE 3: Two-Story Lookout Tower (modX 2..5, modZ 9..12) ---
                        if (numHouses >= 3 && modX >= 2 && modX <= 5 && modZ >= 9 && modZ <= 12) {
                            chunk.setBlockState(pos.set(x, 64, z), cobble, false);
                            if ((modX == 2 || modX == 5) && (modZ == 9 || modZ == 12)) {
                                for (int hy = 65; hy <= 71; hy++) chunk.setBlockState(pos.set(x, hy, z), oakLog, false);
                            } else if (modX == 2 || modX == 5 || modZ == 9 || modZ == 12) {
                                for (int hy = 65; hy <= 71; hy++) {
                                    if (hy == 69) chunk.setBlockState(pos.set(x, hy, z), glassPane, false);
                                    else chunk.setBlockState(pos.set(x, hy, z), oakPlanks, false);
                                }
                            }
                            chunk.setBlockState(pos.set(x, 72, z), cobble, false);
                            if (modX == 3 && modZ == 10) {
                                chunk.setBlockState(pos.set(x, 65, z), chest, false);
                                chunk.setBlockState(pos.set(x, 66, z), bookshelf, false);
                            }
                        }

                        // --- HOUSE 4: Blacksmith & Furnace Workshop (modX 9..13, modZ 9..13) ---
                        if (numHouses >= 4 && modX >= 9 && modX <= 13 && modZ >= 9 && modZ <= 13) {
                            chunk.setBlockState(pos.set(x, 64, z), cobble, false);
                            if (modX == 9 || modX == 13 || modZ == 9 || modZ == 13) {
                                chunk.setBlockState(pos.set(x, 65, z), cobble, false);
                                chunk.setBlockState(pos.set(x, 66, z), cobble, false);
                                chunk.setBlockState(pos.set(x, 67, z), cobble, false);
                            }
                            if (modX == 10 && modZ == 10) chunk.setBlockState(pos.set(x, 65, z), furnace, false);
                            if (modX == 11 && modZ == 10) chunk.setBlockState(pos.set(x, 65, z), craftingTable, false);
                            if (modX == 11 && modZ == 11) chunk.setBlockState(pos.set(x, 65, z), chest, false);
                        }

                        // --- HOUSE 5: Open Wooden Storage Barn (modX 6..9, modZ 7..10) ---
                        if (numHouses == 5 && modX >= 6 && modX <= 9 && modZ >= 7 && modZ <= 10) {
                            chunk.setBlockState(pos.set(x, 64, z), darkEarth, false);
                            if ((modX == 6 || modX == 9) && (modZ == 7 || modZ == 10)) {
                                chunk.setBlockState(pos.set(x, 65, z), oakLog, false);
                                chunk.setBlockState(pos.set(x, 66, z), oakLog, false);
                            }
                            chunk.setBlockState(pos.set(x, 67, z), oakPlanks, false);
                            if (modX == 7 && modZ == 8) chunk.setBlockState(pos.set(x, 65, z), hayBlock, false);
                            if (modX == 8 && modZ == 8) chunk.setBlockState(pos.set(x, 65, z), cassettePlayer, false);
                        }
                        continue;
                    }

                    // ==========================================
                    // 5. STANDARD MONOYELLOW & TRAP ROOMS (Y=65..67, Height 3-4)
                    // ==========================================
                    // Floor (Y=64)
                    if (roomType == TYPE_TRAP_ROOM && ((modX == 7 && modZ == 7) || (modX == 8 && modZ == 8))) {
                        chunk.setBlockState(pos.set(x, 62, z), air, false);
                        chunk.setBlockState(pos.set(x, 63, z), air, false);
                        chunk.setBlockState(pos.set(x, 64, z), ghostCarpet, false);
                    } else {
                        chunk.setBlockState(pos.set(x, 64, z), carpet, false);
                    }

                    // Single Door Wall Logic for Closed Rooms
                    int doorSide = getSingleDoorSide(cellX, cellZ);
                    boolean isWall = isClosedRoomWall(cellX, cellZ, modX, modZ, doorSide);

                    for (int y = 65; y <= 67; y++) {
                        if (isWall) {
                            chunk.setBlockState(pos.set(x, y, z), (y == 65) ? baseboard : wallpaper, false);
                        } else {
                            chunk.setBlockState(pos.set(x, y, z), air, false);
                        }
                    }

                    // Items in storage/standard rooms
                    if (roomType == TYPE_STANDARD_MONOYELLOW && (cellX * 13 + cellZ * 7) % 5 == 0) {
                        if (modX == 7 && modZ == 7) {
                            chunk.setBlockState(pos.set(x, 65, z), chest, false);
                        } else if (modX == 8 && modZ == 8) {
                            chunk.setBlockState(pos.set(x, 65, z), cassettePlayer, false);
                        }
                    }

                    // Directional Signs on walls
                    if (isWall && (modX == 4 || modZ == 4)) {
                        chunk.setBlockState(pos.set(x, 66, z), signBlock, false);
                    }

                    // Ceiling (Y=68)
                    if ((modX % 4 == 3 && modZ % 4 == 3) && !isWall) {
                        if (roomType == TYPE_TRAP_ROOM) {
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

            // Doorway Double Door Placement (1 Door per Closed Room ONLY)
            placeDoorsInChunk(chunk, chunkX, chunkZ);

            return chunk;
        }, executor);
    }

    private static boolean isClosedRoomWall(int cellX, int cellZ, int modX, int modZ, int doorSide) {
        if (modX == 0 || modX == CELL_SIZE - 1 || modZ == 0 || modZ == CELL_SIZE - 1) {
            // Check single entrance doorway (offset 7, 8 along designated wall side)
            if (doorSide == 0 && modX == 0 && (modZ == 7 || modZ == 8)) return false;
            if (doorSide == 1 && modX == CELL_SIZE - 1 && (modZ == 7 || modZ == 8)) return false;
            if (doorSide == 2 && modZ == 0 && (modX == 7 || modX == 8)) return false;
            if (doorSide == 3 && modZ == CELL_SIZE - 1 && (modX == 7 || modX == 8)) return false;
            return true;
        }
        return false;
    }

    private static void placeDoorsInChunk(ChunkAccess chunk, int chunkX, int chunkZ) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int x = 0; x < 16; x++) {
            int worldX = chunkX * 16 + x;
            for (int z = 0; z < 16; z++) {
                int worldZ = chunkZ * 16 + z;
                int modX = Math.floorMod(worldX, CELL_SIZE);
                int modZ = Math.floorMod(worldZ, CELL_SIZE);

                int cellX = Math.floorDiv(worldX, CELL_SIZE);
                int cellZ = Math.floorDiv(worldZ, CELL_SIZE);

                // West wall entrance door (modX == 0, modZ 7,8)
                if (modX == 0 && (modZ == 7 || modZ == 8) && isClosedRoom(cellX, cellZ) && getSingleDoorSide(cellX, cellZ) == 0) {
                    placeDoubleDoor(chunk, pos, x, z, modZ == 7, Direction.EAST, cellX, cellZ);
                }
                // North wall entrance door (modZ == 0, modX 7,8)
                else if (modZ == 0 && (modX == 7 || modX == 8) && isClosedRoom(cellX, cellZ) && getSingleDoorSide(cellX, cellZ) == 2) {
                    placeDoubleDoor(chunk, pos, x, z, modX == 7, Direction.SOUTH, cellX, cellZ);
                }
            }
        }
    }

    private static void placeDoubleDoor(ChunkAccess chunk, BlockPos.MutableBlockPos pos, int x, int z, boolean isLeftHinge, Direction facing, int cellX, int cellZ) {
        long doorHash = Math.abs((cellX * 17L) ^ (cellZ * 31L));
        Block doorBlock = ModBlocks.YELLOW_WOOD_DOOR.get();
        if (doorHash % 4 == 0) doorBlock = ModBlocks.OFFICE_GLASS_DOOR.get();
        else if (doorHash % 4 == 1) doorBlock = ModBlocks.VENT_METAL_DOOR.get();
        else if (doorHash % 4 == 2) doorBlock = ModBlocks.MOSSY_FOREST_DOOR.get();

        DoorHingeSide hinge = isLeftHinge ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT;

        BlockState doorLower = doorBlock.defaultBlockState()
                .setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER)
                .setValue(DoorBlock.FACING, facing)
                .setValue(DoorBlock.HINGE, hinge);

        BlockState doorUpper = doorBlock.defaultBlockState()
                .setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER)
                .setValue(DoorBlock.FACING, facing)
                .setValue(DoorBlock.HINGE, hinge);

        chunk.setBlockState(pos.set(x, 65, z), doorLower, false);
        chunk.setBlockState(pos.set(x, 66, z), doorUpper, false);
    }
}


