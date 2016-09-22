package com.comze.sanman00.spongeskills.sponge.event;

import com.comze.sanman00.spongeskills.api.event.SkillEvent;
import com.comze.sanman00.spongeskills.api.skill.Skill;
import java.util.Optional;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.cause.Cause;

public class SpongeSkillEvent implements SkillEvent, Event {
    private final Cause cause;
    private final Skill skill;
    
    public SpongeSkillEvent(Cause cause, Skill skill) {
        this.cause = cause;
        this.skill = skill;
    }
    
    @Override
    public Optional<? extends Object> getEventCause() {
        return Optional.of(getCause());
    }

    @Override
    public Skill getSkill() {
        return this.skill;
    }

    @Override
    public Cause getCause() {
        return this.cause;
    }    
}
