package com.comze.sanman00.spongeskills.sponge.player;

import com.comze.sanman00.spongeskills.api.player.PlayerWrapper;
import com.comze.sanman00.spongeskills.api.skill.Skill;
import com.comze.sanman00.spongeskills.api.skill.experience.SkillExperience;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import org.spongepowered.api.entity.living.player.Player;

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
         
    }

    @Override
    public Collection<SkillExperience> getExperience() {
        //TODO
        List<SkillExperience> list = Lists.newArrayList();
        return ImmutableList.copyOf(list);
    }
}
