package com.comze.sanman00.spongeskills.sponge.player;

import com.comze.sanman00.spongeskills.api.player.PlayerWrapper;
import com.comze.sanman00.spongeskills.api.skill.Skill;
import com.comze.sanman00.spongeskills.api.skill.experience.SkillExperience;
import com.comze.sanman00.spongeskills.sponge.event.SpongePlayerExperienceChangeEvent;
import com.comze.sanman00.spongeskills.sponge.event.SpongePlayerLevelChangeEvent;
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
    public Player getWrappedPlayer() {
        return this.player;
    }

    @Override
    public void levelUp(int amount, Skill skill) {
        SkillExperience skillExp = this.getExperience().get(skill);
        Sponge.getEventManager().post(SpongePlayerLevelChangeEvent.builder()
                .cause(Cause.of(NamedCause.simulated(this.player), NamedCause.source(this)))
                .skill(skill)
                .player(this)
                .levelBefore(skillExp.getLevel())
                .levelAfter(skillExp.getLevel() + amount)
                .build()
        );
    }

    @Override
    public void giveExperience(int amount, Skill skill) {
        SkillExperience skillExp = getExperience().get(skill);
        //TODO move into handler
        //skillExp.totalExp += amount;
        
        Sponge.getEventManager().post(SpongePlayerExperienceChangeEvent
                .builder()
                .cause(Cause.of(NamedCause.simulated(this.player), NamedCause.source(this)))
                .skill(skill)
                .player(this)
                .expBefore(skillExp.getTotalExperience())
                .expAfter(skillExp.getTotalExperience() + amount)
                .build()
        );
    }

    @Override
    public Map<Skill, SkillExperience> getExperience() {
        //TODO 
        return null;
    }
}
