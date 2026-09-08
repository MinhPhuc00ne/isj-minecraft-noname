package com.yourname.darkgathering.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.util.HashMap;
import java.util.Map;

public class PlayerData implements IPlayerData {
    private PlayerClass playerClass = PlayerClass.NONE;
    private float currentMana = 100.0f;
    private float maxMana = 100.0f;
    private final String[] skillSlots = new String[9];
    private boolean skillHotbarActive = false;
    private final Map<String, Integer> cooldowns = new HashMap<>();

    public PlayerData() {
        for (int i = 0; i < 9; i++) {
            skillSlots[i] = "";
        }
    }

    @Override
    public PlayerClass getPlayerClass() {
        return playerClass;
    }

    @Override
    public void setPlayerClass(PlayerClass playerClass) {
        this.playerClass = playerClass;
    }

    @Override
    public float getCurrentMana() {
        return currentMana;
    }

    @Override
    public void setCurrentMana(float mana) {
        this.currentMana = Math.max(0.0f, Math.min(mana, this.maxMana));
    }

    @Override
    public float getMaxMana() {
        return maxMana;
    }

    @Override
    public void setMaxMana(float maxMana) {
        this.maxMana = Math.max(1.0f, maxMana);
        if (this.currentMana > this.maxMana) {
            this.currentMana = this.maxMana;
        }
    }

    @Override
    public String[] getSkillSlots() {
        return skillSlots;
    }

    @Override
    public String getSkillSlot(int index) {
        if (index >= 0 && index < 9) {
            return skillSlots[index] != null ? skillSlots[index] : "";
        }
        return "";
    }

    @Override
    public void setSkillSlot(int index, String skillId) {
        if (index >= 0 && index < 9) {
            skillSlots[index] = skillId != null ? skillId : "";
        }
    }

    @Override
    public boolean isSkillHotbarActive() {
        return skillHotbarActive;
    }

    @Override
    public void setSkillHotbarActive(boolean active) {
        this.skillHotbarActive = active;
    }

    @Override
    public int getCooldown(String skillId) {
        return cooldowns.getOrDefault(skillId, 0);
    }

    @Override
    public void setCooldown(String skillId, int ticks) {
        if (ticks <= 0) {
            cooldowns.remove(skillId);
        } else {
            cooldowns.put(skillId, ticks);
        }
    }

    @Override
    public void tickCooldowns() {
        if (cooldowns.isEmpty()) return;
        cooldowns.entrySet().removeIf(entry -> {
            int remaining = entry.getValue() - 1;
            if (remaining <= 0) {
                return true;
            } else {
                entry.setValue(remaining);
                return false;
            }
        });
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("PlayerClass", playerClass.name());
        nbt.putFloat("CurrentMana", currentMana);
        nbt.putFloat("MaxMana", maxMana);
        nbt.putBoolean("SkillHotbarActive", skillHotbarActive);

        ListTag slotsTag = new ListTag();
        for (int i = 0; i < 9; i++) {
            slotsTag.add(StringTag.valueOf(skillSlots[i] != null ? skillSlots[i] : ""));
        }
        nbt.put("SkillSlots", slotsTag);

        CompoundTag cooldownsTag = new CompoundTag();
        for (Map.Entry<String, Integer> entry : cooldowns.entrySet()) {
            cooldownsTag.putInt(entry.getKey(), entry.getValue());
        }
        nbt.put("Cooldowns", cooldownsTag);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (nbt.contains("PlayerClass")) {
            try {
                this.playerClass = PlayerClass.valueOf(nbt.getString("PlayerClass"));
            } catch (IllegalArgumentException e) {
                this.playerClass = PlayerClass.NONE;
            }
        }
        if (nbt.contains("CurrentMana")) this.currentMana = nbt.getFloat("CurrentMana");
        if (nbt.contains("MaxMana")) this.maxMana = nbt.getFloat("MaxMana");
        if (nbt.contains("SkillHotbarActive")) this.skillHotbarActive = nbt.getBoolean("SkillHotbarActive");

        if (nbt.contains("SkillSlots", Tag.TAG_LIST)) {
            ListTag slotsTag = nbt.getList("SkillSlots", Tag.TAG_STRING);
            for (int i = 0; i < 9 && i < slotsTag.size(); i++) {
                this.skillSlots[i] = slotsTag.getString(i);
            }
        }

        cooldowns.clear();
        if (nbt.contains("Cooldowns", Tag.TAG_COMPOUND)) {
            CompoundTag cooldownsTag = nbt.getCompound("Cooldowns");
            for (String key : cooldownsTag.getAllKeys()) {
                cooldowns.put(key, cooldownsTag.getInt(key));
            }
        }
    }
}
