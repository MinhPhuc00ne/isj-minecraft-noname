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

    public static final RegistryObject<EntityType<GraduateSpiritEntity>> GRADUATE_BASE = ENTITY_TYPES.register("graduate_base",
            () -> EntityType.Builder.of(GraduateSpiritEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.95F)
                    .build("graduate_base"));

    public static final RegistryObject<EntityType<AsuraSpiritEntity>> ASURA_SPIRIT = ENTITY_TYPES.register("asura_spirit",
            () -> EntityType.Builder.of(AsuraSpiritEntity::new, MobCategory.CREATURE)
                    .sized(0.7F, 2.1F)
                    .build("asura_spirit"));

    public static final RegistryObject<EntityType<NurseSpiritEntity>> NURSE_SPIRIT = ENTITY_TYPES.register("nurse_spirit",
            () -> EntityType.Builder.of(NurseSpiritEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.85F)
                    .build("nurse_spirit"));

    public static final RegistryObject<EntityType<HeadSpiritEntity>> HEAD_SPIRIT = ENTITY_TYPES.register("head_spirit",
            () -> EntityType.Builder.of(HeadSpiritEntity::new, MobCategory.CREATURE)
                    .sized(1.2F, 1.2F)
                    .build("head_spirit"));

    public static final RegistryObject<EntityType<ShadowChildEntity>> SHADOW_CHILD = ENTITY_TYPES.register("shadow_child",
            () -> EntityType.Builder.of(ShadowChildEntity::new, MobCategory.CREATURE)
                    .sized(0.5F, 1.2F)
                    .build("shadow_child"));

    public static final RegistryObject<EntityType<OiranSpiritEntity>> OIRAN_SPIRIT = ENTITY_TYPES.register("oiran_spirit",
            () -> EntityType.Builder.of(OiranSpiritEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.9F)
                    .build("oiran_spirit"));

    public static final RegistryObject<EntityType<SummonRitualEntity>> SUMMON_RITUAL = ENTITY_TYPES.register("summon_ritual",
            () -> EntityType.Builder.<SummonRitualEntity>of(SummonRitualEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .build("summon_ritual"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(EVIL_SPIRIT.get(), EvilSpiritEntity.createAttributes().build());
        event.put(GRADUATE_BASE.get(), GraduateSpiritEntity.createBaseSpiritAttributes().build());
        event.put(ASURA_SPIRIT.get(), AsuraSpiritEntity.createAttributes().build());
        event.put(NURSE_SPIRIT.get(), NurseSpiritEntity.createAttributes().build());
        event.put(HEAD_SPIRIT.get(), HeadSpiritEntity.createAttributes().build());
        event.put(SHADOW_CHILD.get(), ShadowChildEntity.createAttributes().build());
        event.put(OIRAN_SPIRIT.get(), OiranSpiritEntity.createAttributes().build());
    }
}
