package com.yourname.darkgathering.skill;

import com.yourname.darkgathering.capability.IPlayerData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SkillRegistry {
    private static final Map<String, Skill> SKILLS = new HashMap<>();

    public static final Skill SPIRIT_SIGHT = register(new Skill(
            "spirit_sight",
            "Tầm Nhìn Âm Dương",
            "Cấp hiệu ứng Phát Sáng cho tất cả Ác Linh xung quanh trong 10 giây.",
            25.0f,
            200, // 10 seconds cooldown
            Skill.Type.INSTANT
    ) {
        @Override
        public boolean execute(ServerPlayer player, IPlayerData data) {
            player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 0, false, false));
            player.sendSystemMessage(Component.literal("§a[Skill] Đã kích hoạt Tầm Nhìn Âm Dương!"));
            return true;
        }
    });

    public static final Skill SPIRIT_SLASH = register(new Skill(
            "spirit_slash",
            "Liên Trảm Ác Linh",
            "Tăng tốc độ di chuyển và sức mạnh tấn công trong 15 giây.",
            40.0f,
            300, // 15 seconds cooldown
            Skill.Type.CONTINUOUS
    ) {
        @Override
        public boolean execute(ServerPlayer player, IPlayerData data) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, 1, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 300, 0, false, false));
            player.sendSystemMessage(Component.literal("§c[Skill] Đã kích hoạt Liên Trảm Ác Linh!"));
            return true;
        }
    });

    public static final Skill SPECTRAL_SHIELD = register(new Skill(
            "spectral_shield",
            "Lá Chắn Linh Lực",
            "Tạo một lớp kháng sát thương và hấp thụ sát thương trong 20 giây.",
            50.0f,
            400, // 20 seconds cooldown
            Skill.Type.CONTINUOUS
    ) {
        @Override
        public boolean execute(ServerPlayer player, IPlayerData data) {
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 400, 1, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 400, 2, false, false));
            player.sendSystemMessage(Component.literal("§b[Skill] Đã tạo Lá Chắn Linh Lực!"));
            return true;
        }
    });

    public static Skill register(Skill skill) {
        SKILLS.put(skill.getId(), skill);
        return skill;
    }

    public static Skill getSkill(String id) {
        return SKILLS.get(id);
    }

    public static Collection<Skill> getAllSkills() {
        return SKILLS.values();
    }
}
