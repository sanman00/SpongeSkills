package com.comze.sanman00.spongeskills.sponge.player;

import com.comze.sanman00.spongeskills.api.player.PlayerWrapper;
import com.comze.sanman00.spongeskills.api.skill.Skill;
import com.comze.sanman00.spongeskills.api.skill.experience.SkillExperience;
import com.comze.sanman00.spongeskills.sponge.data.PluginKeys;
import com.comze.sanman00.spongeskills.sponge.event.SpongePlayerExperienceChangeEvent;
import com.comze.sanman00.spongeskills.sponge.event.SpongePlayerLevelChangeEvent;
import com.google.inject.internal.util.ImmutableMap;
import java.util.Map;
import java.util.UUID;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;

public final class SpongePlayerWrapper implements PlayerWrapper<Player> {
    private final UUID uuid;
    
    public SpongePlayerWrapper(Player player) {
        this(player.getUniqueId());
    }
    
    public SpongePlayerWrapper(UUID uuid) {
        if (!Sponge.getServer().getPlayer(uuid).isPresent()) {
            throw new IllegalArgumentException("Expected a valid player UUID");
        }
        this.uuid = uuid;
    }
    
    @Override
    public Player getWrappedPlayer() {
        return Sponge.getServer().getPlayer(this.uuid).orElseThrow(() -> new IllegalStateException("Invalid UUID for player"));
    }

    @Override
    public void levelUp(int amount, Skill skill) {
        SkillExperience skillExp = this.getExperience().get(skill);
        Sponge.getEventManager().post(SpongePlayerLevelChangeEvent.builder()
                .cause(Cause.of(NamedCause.simulated(this.uuid), NamedCause.source(this)))
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
                .cause(Cause.of(NamedCause.simulated(this.uuid), NamedCause.source(this)))
                .skill(skill)
                .player(this)
                .expBefore(skillExp.getTotalExperience())
                .expAfter(skillExp.getTotalExperience() + amount)
                .build()
        );
    }

    @Override
    public Map<Skill, SkillExperience> getExperience() {
        return ImmutableMap.copyOf(this.getWrappedPlayer().get(PluginKeys.SKILL_EXPERIENCE).get());
    }

    @Override
    public UUID getPlayerUUID() {
        return this.uuid;
    }
}
