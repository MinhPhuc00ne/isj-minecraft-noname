package com.yourname.backrooms.world;

import com.yourname.backrooms.BackroomsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class ModDimensions {
    public static final ResourceKey<Level> BACKROOMS_LEVEL_0 = ResourceKey.create(
            Registries.DIMENSION,
            new ResourceLocation(BackroomsMod.MOD_ID, "level_0")
    );

    public static final ResourceKey<DimensionType> BACKROOMS_LEVEL_0_TYPE = ResourceKey.create(
            Registries.DIMENSION_TYPE,
            new ResourceLocation(BackroomsMod.MOD_ID, "level_0_type")
    );
}
