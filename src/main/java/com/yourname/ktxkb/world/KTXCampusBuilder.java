package com.yourname.ktxkb.world;

import com.yourname.ktxkb.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class KTXCampusBuilder {

    public static void buildFullCampus(Level level, BlockPos origin) {
        int baseX = origin.getX();
        int baseY = origin.getY();
        int baseZ = origin.getZ();

        BlockState coalRoad = Blocks.COAL_BLOCK.defaultBlockState();
        BlockState yellowLine = Blocks.YELLOW_CONCRETE.defaultBlockState();
        BlockState whiteLine = Blocks.WHITE_CONCRETE.defaultBlockState();
        BlockState sidewalk = Blocks.LIGHT_GRAY_CONCRETE.defaultBlockState();
        BlockState curb = Blocks.SMOOTH_STONE_SLAB.defaultBlockState();
        BlockState blueConcrete = Blocks.BLUE_CONCRETE.defaultBlockState();
        BlockState whiteConcrete = Blocks.WHITE_CONCRETE.defaultBlockState();
        BlockState yellowConcrete = Blocks.YELLOW_CONCRETE.defaultBlockState();
        BlockState pinkConcrete = Blocks.PINK_CONCRETE.defaultBlockState();
        BlockState glassPane = Blocks.BLACK_STAINED_GLASS_PANE.defaultBlockState();
        BlockState clearGlass = Blocks.GLASS_PANE.defaultBlockState();
        BlockState water = Blocks.WATER.defaultBlockState();
        BlockState redConcrete = Blocks.RED_CONCRETE.defaultBlockState();
        BlockState grass = Blocks.GRASS_BLOCK.defaultBlockState();
        BlockState ironBars = Blocks.IRON_BARS.defaultBlockState();
        BlockState stonePillar = Blocks.SMOOTH_STONE.defaultBlockState();
        BlockState granite = Blocks.GRANITE.defaultBlockState();

        // 1. Build Perimeter Boundary Wall & Fence (X: -75..75, Z: -25..195)
        buildPerimeterWall(level, baseX, baseY, baseZ);

        // 2. Build Main 2-Lane Road with Dashed Center Markings (Z: -24 to 190)
        for (int z = -24; z <= 190; z++) {
            for (int x = -5; x <= 5; x++) {
                level.setBlock(new BlockPos(baseX + x, baseY, baseZ + z), coalRoad, 3);
            }

            // Dashed Yellow Center Line
            if (Math.abs(z % 4) < 2) {
                level.setBlock(new BlockPos(baseX, baseY, baseZ + z), yellowLine, 3);
            }

            // Sidewalks & Curbs
            level.setBlock(new BlockPos(baseX - 6, baseY, baseZ + z), sidewalk, 3);
            level.setBlock(new BlockPos(baseX - 7, baseY, baseZ + z), sidewalk, 3);
            level.setBlock(new BlockPos(baseX + 6, baseY, baseZ + z), sidewalk, 3);
            level.setBlock(new BlockPos(baseX + 7, baseY, baseZ + z), sidewalk, 3);

            // Solar Streetlamps every 15 blocks
            if (z % 15 == 0 && z > -15 && z < 185) {
                buildSolarLamp(level, new BlockPos(baseX - 7, baseY + 1, baseZ + z));
                buildSolarLamp(level, new BlockPos(baseX + 7, baseY + 1, baseZ + z));
            }
        }

        // 3. Zebra Crossings (Vạch sang đường)
        buildZebraCrossing(level, baseX, baseY, baseZ - 10);
        buildZebraCrossing(level, baseX, baseY, baseZ + 12);
        buildZebraCrossing(level, baseX, baseY, baseZ + 68);
        buildZebraCrossing(level, baseX, baseY, baseZ + 155);

        // 4. Authentic V-Gate & Guard Booth (Matching images (3).jpg at Z = -15)
        buildAuthenticGateAndGuardBooth(level, new BlockPos(baseX, baseY + 1, baseZ - 15));

        // 5. Central Athletic Field & Running Track (Z: 15..60)
        for (int z = 15; z <= 60; z++) {
            for (int x = -35; x <= 35; x++) {
                if (Math.abs(x) >= 8) {
                    // Outer running track
                    if (Math.abs(x) == 8 || Math.abs(x) == 35 || z == 15 || z == 60) {
                        level.setBlock(new BlockPos(baseX + x, baseY, baseZ + z), redConcrete, 3);
                    } else if (Math.abs(x) == 9 || z == 16 || z == 59) {
                        level.setBlock(new BlockPos(baseX + x, baseY, baseZ + z), whiteLine, 3);
                    } else {
                        level.setBlock(new BlockPos(baseX + x, baseY, baseZ + z), grass, 3);
                    }
                }
            }
        }

        // 6. Central Roundabout & Star Monument (Z: 70..100)
        buildRoundaboutAndStar(level, new BlockPos(baseX, baseY + 1, baseZ + 85));

        // 7. Build 24 Dormitory Buildings (15-Story 1:1)
        // --- CLUSTER A (Left Front) ---
        buildDormitoryBlock(level, baseX - 38, baseY + 1, baseZ + 5, 14, 18, 18, ModBlocks.A1_SIGN.get(), whiteConcrete, yellowConcrete);
        buildDormitoryBlock(level, baseX - 63, baseY + 1, baseZ + 5, 14, 18, 18, ModBlocks.A2_SIGN.get(), whiteConcrete, yellowConcrete);
        buildDormitoryBlock(level, baseX - 38, baseY + 1, baseZ + 30, 14, 18, 18, ModBlocks.A3_SIGN.get(), whiteConcrete, yellowConcrete);
        buildDormitoryBlock(level, baseX - 63, baseY + 1, baseZ + 30, 14, 18, 18, ModBlocks.A4_SIGN.get(), whiteConcrete, yellowConcrete);
        buildDormitoryBlock(level, baseX - 50, baseY + 1, baseZ + 55, 20, 18, 18, ModBlocks.A5_SIGN.get(), whiteConcrete, yellowConcrete);

        // --- CLUSTER B (Right Front) ---
        buildDormitoryBlock(level, baseX + 24, baseY + 1, baseZ + 5, 14, 18, 18, ModBlocks.B1_SIGN.get(), whiteConcrete, yellowConcrete);
        buildDormitoryBlock(level, baseX + 49, baseY + 1, baseZ + 5, 14, 18, 18, ModBlocks.B2_SIGN.get(), whiteConcrete, yellowConcrete);
        buildDormitoryBlock(level, baseX + 24, baseY + 1, baseZ + 30, 14, 18, 18, ModBlocks.B3_SIGN.get(), whiteConcrete, yellowConcrete);
        buildDormitoryBlock(level, baseX + 49, baseY + 1, baseZ + 30, 14, 18, 18, ModBlocks.B4_SIGN.get(), whiteConcrete, yellowConcrete);
        buildDormitoryBlock(level, baseX + 36, baseY + 1, baseZ + 55, 20, 18, 18, ModBlocks.B5_SIGN.get(), whiteConcrete, yellowConcrete);

        // --- CLUSTER C (Left Middle) ---
        buildDormitoryBlock(level, baseX - 38, baseY + 1, baseZ + 90, 14, 18, 18, ModBlocks.C1_SIGN.get(), whiteConcrete, yellowConcrete);
        buildDormitoryBlock(level, baseX - 63, baseY + 1, baseZ + 90, 14, 18, 18, ModBlocks.C2_SIGN.get(), whiteConcrete, yellowConcrete);
        buildDormitoryBlock(level, baseX - 38, baseY + 1, baseZ + 115, 14, 18, 18, ModBlocks.C3_SIGN.get(), whiteConcrete, yellowConcrete);
        buildDormitoryBlock(level, baseX - 63, baseY + 1, baseZ + 115, 14, 18, 18, ModBlocks.C4_SIGN.get(), whiteConcrete, yellowConcrete);
        buildDormitoryBlock(level, baseX - 38, baseY + 1, baseZ + 140, 14, 18, 18, ModBlocks.C5_SIGN.get(), whiteConcrete, yellowConcrete);
        buildDormitoryBlock(level, baseX - 63, baseY + 1, baseZ + 140, 14, 18, 18, ModBlocks.C6_SIGN.get(), whiteConcrete, yellowConcrete);

        // --- CLUSTER D (Right Middle) ---
        buildDormitoryBlock(level, baseX + 24, baseY + 1, baseZ + 90, 14, 18, 18, ModBlocks.D2_SIGN.get(), whiteConcrete, yellowConcrete);
        buildDormitoryBlock(level, baseX + 49, baseY + 1, baseZ + 90, 14, 18, 18, ModBlocks.D3_SIGN.get(), whiteConcrete, yellowConcrete);
        buildDormitoryBlock(level, baseX + 24, baseY + 1, baseZ + 115, 14, 18, 18, ModBlocks.D4_SIGN.get(), whiteConcrete, yellowConcrete);
        buildDormitoryBlock(level, baseX + 49, baseY + 1, baseZ + 115, 14, 18, 18, ModBlocks.D5_SIGN.get(), whiteConcrete, yellowConcrete);
        buildDormitoryBlock(level, baseX + 36, baseY + 1, baseZ + 140, 20, 18, 18, ModBlocks.D6_SIGN.get(), whiteConcrete, yellowConcrete);

        // --- REAR HIGHLIGHT CLUSTER (Pink/White/Yellow) ---
        buildDormitoryBlock(level, baseX - 50, baseY + 1, baseZ + 168, 16, 20, 18, ModBlocks.E1_SIGN.get(), pinkConcrete, yellowConcrete);
        buildDormitoryBlock(level, baseX - 22, baseY + 1, baseZ + 168, 16, 20, 18, ModBlocks.F1_SIGN.get(), pinkConcrete, yellowConcrete);
        buildDormitoryBlock(level, baseX + 6, baseY + 1, baseZ + 168, 16, 20, 18, ModBlocks.F2_SIGN.get(), pinkConcrete, yellowConcrete);
        buildDormitoryBlock(level, baseX + 34, baseY + 1, baseZ + 168, 16, 20, 18, ModBlocks.G1_SIGN.get(), pinkConcrete, yellowConcrete);

        // 8. Rear Gate
        buildRearGate(level, new BlockPos(baseX, baseY + 1, baseZ + 195));
    }

    private static void buildPerimeterWall(Level level, int baseX, int baseY, int baseZ) {
        BlockState white = Blocks.WHITE_CONCRETE.defaultBlockState();
        BlockState blue = Blocks.BLUE_CONCRETE.defaultBlockState();
        BlockState ironBars = Blocks.IRON_BARS.defaultBlockState();
        BlockState pillar = Blocks.SMOOTH_STONE.defaultBlockState();

        int minX = baseX - 75;
        int maxX = baseX + 75;
        int minZ = baseZ - 25;
        int maxZ = baseZ + 195;

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                if (x == minX || x == maxX || z == minZ || z == maxZ) {
                    // Leave openings for Main Gate & Rear Gate
                    if (z == minZ && Math.abs(x - baseX) <= 12) continue;
                    if (z == maxZ && Math.abs(x - baseX) <= 8) continue;

                    BlockPos p0 = new BlockPos(x, baseY + 1, z);
                    BlockPos p1 = new BlockPos(x, baseY + 2, z);
                    BlockPos p2 = new BlockPos(x, baseY + 3, z);
                    BlockPos p3 = new BlockPos(x, baseY + 4, z);

                    if ((x % 6 == 0) || (z % 6 == 0)) {
                        level.setBlock(p0, pillar, 3);
                        level.setBlock(p1, pillar, 3);
                        level.setBlock(p2, pillar, 3);
                        level.setBlock(p3, pillar, 3);
                    } else {
                        level.setBlock(p0, blue, 3);
                        level.setBlock(p1, white, 3);
                        level.setBlock(p2, ironBars, 3);
                        level.setBlock(p3, ironBars, 3);
                    }
                }
            }
        }
    }

    private static void buildZebraCrossing(Level level, int baseX, int baseY, int z) {
        BlockState white = Blocks.WHITE_CONCRETE.defaultBlockState();
        BlockState coal = Blocks.COAL_BLOCK.defaultBlockState();

        for (int dz = 0; dz < 4; dz++) {
            for (int x = -5; x <= 5; x++) {
                if (dz % 2 == 0) {
                    level.setBlock(new BlockPos(baseX + x, baseY, z + dz), white, 3);
                } else {
                    level.setBlock(new BlockPos(baseX + x, baseY, z + dz), coal, 3);
                }
            }
        }
    }

    private static void buildAuthenticGateAndGuardBooth(Level level, BlockPos pos) {
        BlockState blue = Blocks.BLUE_CONCRETE.defaultBlockState();
        BlockState white = Blocks.WHITE_CONCRETE.defaultBlockState();
        BlockState glass = Blocks.GLASS_PANE.defaultBlockState();
        BlockState granite = Blocks.GRANITE.defaultBlockState();

        // Security Guard Booth under gate (Left side: X = -9..-4, Z = -2..2)
        for (int x = -9; x <= -4; x++) {
            for (int z = -2; z <= 2; z++) {
                for (int h = 0; h <= 4; h++) {
                    BlockPos bp = pos.offset(x, h, z);
                    if (h == 0) {
                        level.setBlock(bp, white, 3);
                    } else if (h == 1 || h == 2) {
                        if (x == -9 || x == -4 || z == -2 || z == 2) {
                            level.setBlock(bp, glass, 3);
                        } else {
                            level.setBlock(bp, Blocks.AIR.defaultBlockState(), 3);
                        }
                    } else {
                        level.setBlock(bp, white, 3); // Roof
                    }
                }
            }
        }

        // Giant V-Shaped Blue Arch
        for (int h = 0; h <= 10; h++) {
            level.setBlock(pos.offset(-12 + h, h, 0), blue, 3);
            level.setBlock(pos.offset(-11 + h, h, 0), blue, 3);
            level.setBlock(pos.offset(12 - h, h, 0), blue, 3);
            level.setBlock(pos.offset(11 - h, h, 0), blue, 3);
        }

        // Overhead Spanning Canopy Beam
        for (int x = -12; x <= 12; x++) {
            level.setBlock(pos.offset(x, 11, 0), blue, 3);
            level.setBlock(pos.offset(x, 12, 0), white, 3);
        }

        // Granite Signboard Wall next to gate (Right side: X = 4..12)
        for (int x = 4; x <= 12; x++) {
            for (int h = 0; h <= 4; h++) {
                level.setBlock(pos.offset(x, h, -1), granite, 3);
            }
        }

        // Mount Gate Signboard & VNU Logo Block
        level.setBlock(pos.offset(6, 2, -2), ModBlocks.CONG_TRUOC_SIGN.get().defaultBlockState(), 3);
        level.setBlock(pos.offset(10, 2, -2), ModBlocks.VNU_LOGO_BLOCK.get().defaultBlockState(), 3);
    }

    private static void buildDormitoryBlock(Level level, int x, int y, int z, int width, int depth, int height, Block signBlock, BlockState wallState, BlockState accentState) {
        BlockState blueState = Blocks.BLUE_CONCRETE.defaultBlockState();
        BlockState glassPane = Blocks.BLACK_STAINED_GLASS_PANE.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState quartz = Blocks.SMOOTH_QUARTZ.defaultBlockState();

        for (int dx = 0; dx < width; dx++) {
            for (int dz = 0; dz < depth; dz++) {
                for (int dh = 0; dh < height; dh++) {
                    BlockPos p = new BlockPos(x + dx, y + dh, z + dz);
                    
                    if (dx == 0 || dx == width - 1 || dz == 0 || dz == depth - 1) {
                        if (dh == 0 || dh == height - 1) {
                            level.setBlock(p, blueState, 3); // Base & Roof rim
                        } else if (dx % 3 == 0) {
                            level.setBlock(p, accentState, 3); // Vertical stripes
                        } else if (dh % 2 == 1 && dx % 2 == 1) {
                            level.setBlock(p, glassPane, 3); // Windows
                        } else {
                            level.setBlock(p, wallState, 3);
                        }
                    } else {
                        if (dh % 3 == 0) {
                            level.setBlock(p, wallState, 3); // Floor slabs
                        } else {
                            level.setBlock(p, air, 3);
                        }
                    }
                }
            }
        }

        // Roof Helipad / Elevator Tower
        for (int rx = width / 2 - 2; rx <= width / 2 + 2; rx++) {
            for (int rz = depth / 2 - 2; rz <= depth / 2 + 2; rz++) {
                level.setBlock(new BlockPos(x + rx, y + height, z + rz), quartz, 3);
                level.setBlock(new BlockPos(x + rx, y + height + 1, z + rz), quartz, 3);
            }
        }

        // Mount Building Identification Sign on top facade
        level.setBlock(new BlockPos(x + width / 2, y + height - 2, z - 1), signBlock.defaultBlockState(), 3);
        // Mount Lobby Header Sign above ground floor entrance
        level.setBlock(new BlockPos(x + width / 2, y + 2, z - 1), ModBlocks.LOBBY_HEADER_SIGN.get().defaultBlockState(), 3);
    }

    private static void buildRoundaboutAndStar(Level level, BlockPos center) {
        BlockState water = Blocks.WATER.defaultBlockState();
        BlockState red = Blocks.RED_CONCRETE.defaultBlockState();
        BlockState yellow = Blocks.YELLOW_CONCRETE.defaultBlockState();

        // Pool (Semi-circle)
        for (int rx = -8; rx <= 8; rx++) {
            for (int rz = -8; rz <= 0; rz++) {
                if (rx * rx + rz * rz <= 64) {
                    level.setBlock(center.offset(rx, -1, rz), red, 3);
                    level.setBlock(center.offset(rx, 0, rz), water, 3);
                }
            }
        }

        // Star Monument Pedestal
        level.setBlock(center.offset(0, 1, 3), red, 3);
        level.setBlock(center.offset(0, 2, 3), yellow, 3);
    }

    private static void buildRearGate(Level level, BlockPos pos) {
        BlockState blue = Blocks.BLUE_CONCRETE.defaultBlockState();
        for (int h = 0; h <= 6; h++) {
            level.setBlock(pos.offset(-6, h, 0), blue, 3);
            level.setBlock(pos.offset(6, h, 0), blue, 3);
        }
        for (int x = -6; x <= 6; x++) {
            level.setBlock(pos.offset(x, 7, 0), blue, 3);
        }
        level.setBlock(pos.offset(0, 3, -1), ModBlocks.CONG_SAU_SIGN.get().defaultBlockState(), 3);
    }

    private static void buildSolarLamp(Level level, BlockPos pos) {
        BlockState ironBars = Blocks.IRON_BARS.defaultBlockState();
        BlockState lamp = Blocks.REDSTONE_LAMP.defaultBlockState();
        BlockState detector = Blocks.DAYLIGHT_DETECTOR.defaultBlockState();

        level.setBlock(pos, ironBars, 3);
        level.setBlock(pos.above(), ironBars, 3);
        level.setBlock(pos.above(2), ironBars, 3);
        level.setBlock(pos.above(3), lamp, 3);
        level.setBlock(pos.above(4), detector, 3);
    }
}
