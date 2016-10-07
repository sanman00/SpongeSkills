package com.comze.sanman00.spongeskills.sponge.skill.experience;

import com.comze.sanman00.spongeskills.api.player.PlayerWrapper;
import com.comze.sanman00.spongeskills.api.skill.Skill;
import com.comze.sanman00.spongeskills.api.skill.experience.SkillExperience;
import com.comze.sanman00.spongeskills.sponge.player.SpongePlayerWrapper;

public final class SpongeSkillExperience implements SkillExperience {
    private final Skill skill;
    private final SpongePlayerWrapper player;
    public int level;
    public int totalExp;
    public int remainingExp;
    
    public SpongeSkillExperience(SpongePlayerWrapper player, Skill skill, int level, int totalExp, int remainingExp) {
        this.player = player;
        this.skill = skill;
        this.level = level;
        this.totalExp = totalExp;
        this.remainingExp = remainingExp;
    }
    
    @Override
    public int getTotalExperience() {
        return this.totalExp;
    }

    @Override
    public int getRemainingExperience() {
        return this.remainingExp;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public Skill getSkill() {
        return this.skill;
    }

    @Override
    public PlayerWrapper<?> getPlayer() {
        return this.player;
    }
}
