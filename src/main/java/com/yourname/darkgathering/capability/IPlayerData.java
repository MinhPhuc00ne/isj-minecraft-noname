package com.yourname.darkgathering.capability;

import net.minecraft.nbt.CompoundTag;

public interface IPlayerData {
    enum PlayerClass {
        NONE,
        ONMYOJI,
        EVIL_SPIRIT
    }

    PlayerClass getPlayerClass();
    void setPlayerClass(PlayerClass playerClass);

    float getCurrentMana();
    void setCurrentMana(float mana);

    float getMaxMana();
    void setMaxMana(float maxMana);

    String[] getSkillSlots();
    String getSkillSlot(int index);
    void setSkillSlot(int index, String skillId);

    boolean isSkillHotbarActive();
    void setSkillHotbarActive(boolean active);

    int getCooldown(String skillId);
    void setCooldown(String skillId, int ticks);
    void tickCooldowns();

    CompoundTag serializeNBT();
    void deserializeNBT(CompoundTag nbt);
}
