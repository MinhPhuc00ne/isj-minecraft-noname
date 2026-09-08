package com.yourname.darkgathering.skill;

import com.yourname.darkgathering.capability.IPlayerData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;

public abstract class Skill {
    public enum Type {
        INSTANT,
        CONTINUOUS
    }

    private final String id;
    private final String name;
    private final String description;
    private final float manaCost;
    private final int cooldownTicks;
    private final Type type;

    public Skill(String id, String name, String description, float manaCost, int cooldownTicks, Type type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.manaCost = manaCost;
        this.cooldownTicks = cooldownTicks;
        this.type = type;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public float getManaCost() { return manaCost; }
    public int getCooldownTicks() { return cooldownTicks; }
    public Type getType() { return type; }

    public abstract boolean execute(ServerPlayer player, IPlayerData data);
}
