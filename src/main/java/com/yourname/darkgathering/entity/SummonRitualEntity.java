package com.yourname.darkgathering.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class SummonRitualEntity extends Entity {
    private int ritualTicks = 0;
    private String spiritType = "evil_spirit";
    private UUID ownerUUID;

    public SummonRitualEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public void setSpiritType(String spiritType) {
        this.spiritType = spiritType;
    }

    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        super.tick();
        ritualTicks++;

        if (this.level().isClientSide) {
            // Stage 1 (0s - 3s = 0 - 60 ticks): Chain particles around doll
            if (ritualTicks <= 60) {
                for (int i = 0; i < 4; i++) {
                    double offsetX = (this.random.nextDouble() - 0.5D) * 0.8D;
                    double offsetZ = (this.random.nextDouble() - 0.5D) * 0.8D;
                    this.level().addParticle(ParticleTypes.CRIT, this.getX() + offsetX, this.getY() + 0.2D, this.getZ() + offsetZ, 0, 0.05, 0);
                }
            }
            // Stage 2 (3s - 6s = 60 - 120 ticks): Spreading blood pool particles 3-4 blocks around center
            else if (ritualTicks <= 120) {
                double radius = ((ritualTicks - 60) / 60.0D) * 3.5D;
                for (int i = 0; i < 12; i++) {
                    double angle = this.random.nextDouble() * Math.PI * 2;
                    double r = this.random.nextDouble() * radius;
                    double px = this.getX() + Math.cos(angle) * r;
                    double pz = this.getZ() + Math.sin(angle) * r;
                    this.level().addParticle(ParticleTypes.ANGRY_VILLAGER, px, this.getY() + 0.1D, pz, 0, 0, 0);
                    this.level().addParticle(ParticleTypes.DAMAGE_INDICATOR, px, this.getY() + 0.1D, pz, 0, 0.02, 0);
                }
            }
        } else {
            // Server side: Stage 3 completion at 6s (120 ticks) -> Spawn Graduate Spirit
            if (ritualTicks >= 120) {
                if (this.level() instanceof ServerLevel serverLevel) {
                    GraduateSpiritEntity spirit = createSpiritEntity(serverLevel, spiritType);
                    if (spirit != null) {
                        spirit.setPos(this.getX(), this.getY(), this.getZ());
                        if (ownerUUID != null) {
                            spirit.setOwnerUUID(ownerUUID);
                            spirit.setTame(true);
                        }
                        serverLevel.addFreshEntity(spirit);
                    }
                }
                this.discard();
            }
        }
    }

    private GraduateSpiritEntity createSpiritEntity(ServerLevel level, String type) {
        return switch (type) {
            case "asura_spirit" -> new AsuraSpiritEntity(ModEntities.ASURA_SPIRIT.get(), level);
            case "nurse_spirit" -> new NurseSpiritEntity(ModEntities.NURSE_SPIRIT.get(), level);
            case "head_spirit" -> new HeadSpiritEntity(ModEntities.HEAD_SPIRIT.get(), level);
            case "shadow_child" -> new ShadowChildEntity(ModEntities.SHADOW_CHILD.get(), level);
            case "oiran_spirit" -> new OiranSpiritEntity(ModEntities.OIRAN_SPIRIT.get(), level);
            default -> new GraduateSpiritEntity(ModEntities.GRADUATE_BASE.get(), level);
        };
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("RitualTicks")) this.ritualTicks = tag.getInt("RitualTicks");
        if (tag.contains("SpiritType")) this.spiritType = tag.getString("SpiritType");
        if (tag.hasUUID("OwnerUUID")) this.ownerUUID = tag.getUUID("OwnerUUID");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("RitualTicks", ritualTicks);
        tag.putString("SpiritType", spiritType);
        if (ownerUUID != null) tag.putUUID("OwnerUUID", ownerUUID);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}
