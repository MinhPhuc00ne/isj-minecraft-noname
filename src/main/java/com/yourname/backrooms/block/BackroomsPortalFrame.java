package com.yourname.backrooms.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BackroomsPortalFrame {

    public static boolean trySpawnPortal(Level level, BlockPos pos) {
        for (Direction.Axis axis : new Direction.Axis[]{Direction.Axis.X, Direction.Axis.Z}) {
            if (checkAndBuildPortal(level, pos, axis)) {
                return true;
            }
            if (checkAndBuildPortal(level, pos.above(), axis)) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkAndBuildPortal(Level level, BlockPos startPos, Direction.Axis axis) {
        Direction dirRight = axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;
        Direction dirLeft = dirRight.getOpposite();

        // Find bottom-left corner of opening
        BlockPos corner = startPos;
        int maxScan = 10;

        while (maxScan > 0 && isAirOrFire(level, corner.relative(dirLeft))) {
            corner = corner.relative(dirLeft);
            maxScan--;
        }
        while (maxScan > 0 && isAirOrFire(level, corner.below())) {
            corner = corner.below();
            maxScan--;
        }

        // Measure width of interior
        int width = 0;
        BlockPos temp = corner;
        while (width < 10 && isAirOrFire(level, temp)) {
            width++;
            temp = temp.relative(dirRight);
        }

        if (width < 2) return false; // Minimum width 2 interior blocks

        // Measure height of interior
        int height = 0;
        temp = corner;
        while (height < 10 && isAirOrFire(level, temp)) {
            height++;
            temp = temp.above();
        }

        if (height < 3) return false; // Minimum height 3 interior blocks

        // Validate frame surrounding interior
        // Check bottom border
        for (int w = 0; w < width; w++) {
            BlockPos b = corner.relative(dirRight, w).below();
            if (!isValidFrameBlock(level, b)) return false;
        }
        // Check top border
        for (int w = 0; w < width; w++) {
            BlockPos b = corner.relative(dirRight, w).above(height);
            if (!isValidFrameBlock(level, b)) return false;
        }
        // Check left border
        for (int h = 0; h < height; h++) {
            BlockPos b = corner.above(h).relative(dirLeft);
            if (!isValidFrameBlock(level, b)) return false;
        }
        // Check right border
        for (int h = 0; h < height; h++) {
            BlockPos b = corner.above(h).relative(dirRight, width);
            if (!isValidFrameBlock(level, b)) return false;
        }

        // Fill portal interior
        BlockState portalState = ModBlocks.BACKROOMS_PORTAL.get().defaultBlockState();
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                BlockPos target = corner.relative(dirRight, w).above(h);
                level.setBlock(target, portalState, 3);
            }
        }
        return true;
    }

    private static boolean isAirOrFire(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.isAir() || state.is(Blocks.FIRE);
    }

    private static boolean isValidFrameBlock(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.is(ModBlocks.BACKROOMS_FRAME.get()) ||
                state.is(ModBlocks.YELLOW_WALLPAPER.get()) ||
                state.is(ModBlocks.WALLPAPER_BASEBOARD.get());
    }
}
