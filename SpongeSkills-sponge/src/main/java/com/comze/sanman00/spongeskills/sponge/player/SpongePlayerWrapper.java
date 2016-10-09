package com.comze.sanman00.spongeskills.sponge.player;

import com.comze.sanman00.spongeskills.api.player.PlayerWrapper;
import com.comze.sanman00.spongeskills.api.skill.Skill;
import com.comze.sanman00.spongeskills.api.skill.experience.SkillExperience;
import com.comze.sanman00.spongeskills.sponge.event.SpongePlayerExperienceChangeEvent;
import com.comze.sanman00.spongeskills.sponge.skill.experience.SpongeSkillExperience;
import java.util.Map;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;

public final class SpongePlayerWrapper implements PlayerWrapper<Player> {
    private final Player player;
    
    public SpongePlayerWrapper(Player player) {
        this.player = player;
    }
    
    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public void levelUp(int amount, Skill skill) {
        
    }

    @Override
    public void giveExperience(int amount, Skill skill) {
        SpongeSkillExperience skillExp = (SpongeSkillExperience) getExperience().get(skill);
        skillExp.totalExp += amount;
        
        Sponge.getEventManager().post(new SpongePlayerExperienceChangeEvent(Cause.of(NamedCause.simulated(this.player), NamedCause.source(this)), skill, this, skillExp.totalExp - amount, skillExp.totalExp));
    }

    @Override
    public Map<Skill, SkillExperience> getExperience() {
        //TODO 
        return null;
    }
}
