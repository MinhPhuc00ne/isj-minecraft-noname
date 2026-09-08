package com.yourname.darkgathering.entity;

import com.yourname.darkgathering.DarkGatheringMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = DarkGatheringMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, DarkGatheringMod.MOD_ID);

    public static final RegistryObject<EntityType<EvilSpiritEntity>> EVIL_SPIRIT = ENTITY_TYPES.register("evil_spirit",
            () -> EntityType.Builder.of(EvilSpiritEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .build("evil_spirit"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(EVIL_SPIRIT.get(), EvilSpiritEntity.createAttributes().build());
    }
}
