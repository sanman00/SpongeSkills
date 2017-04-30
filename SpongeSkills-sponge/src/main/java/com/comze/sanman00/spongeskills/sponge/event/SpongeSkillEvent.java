package com.comze.sanman00.spongeskills.sponge.event;

import com.comze.sanman00.spongeskills.api.event.SkillEvent;
import com.comze.sanman00.spongeskills.api.skill.Skill;
import java.util.Optional;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.cause.Cause;

public abstract class SpongeSkillEvent extends AbstractEventBase implements SkillEvent, Cancellable {
    private final Skill skill;
    private boolean cancelled;

    public SpongeSkillEvent(Cause cause, Skill skill) {
        super(cause);
        this.skill = skill;
    }

    @Override
    public Skill getSkill() {
        return this.skill;
    }
    
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
