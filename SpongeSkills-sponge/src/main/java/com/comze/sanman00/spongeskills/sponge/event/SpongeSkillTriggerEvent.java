package com.comze.sanman00.spongeskills.sponge.event;

import com.comze.sanman00.spongeskills.api.event.SkillTriggerEvent;
import com.comze.sanman00.spongeskills.api.skill.Skill;
import com.comze.sanman00.spongeskills.sponge.player.SpongePlayerWrapper;
import org.spongepowered.api.event.cause.Cause;

public class SpongeSkillTriggerEvent extends SpongeSkillEvent implements SkillTriggerEvent {
    public static class Builder {
        private Cause cause;
        private Skill skill;
        private SpongePlayerWrapper player;

        private Builder() {

        }

        public Builder cause(Cause cause) {
            this.cause = cause;
            return this;
        }

        public Builder skill(Skill skill) {
            this.skill = skill;
            return this;
        }
        
        public Builder player(SpongePlayerWrapper player) {
            this.player = player;
            return this;
        }

        public SpongeSkillTriggerEvent build() {
            return new SpongeSkillTriggerEvent(this.cause, this.skill, this.player);
        }
    }
    private final SpongePlayerWrapper player;

    public static SpongeSkillTriggerEvent.Builder builder() {
        return new SpongeSkillTriggerEvent.Builder();
    }

    public SpongeSkillTriggerEvent(Cause cause, Skill skill, SpongePlayerWrapper player) {
        super(cause, skill);
        this.player = player;
    }

    @Override
    public SpongePlayerWrapper getPlayer() {
        return this.player;
    }
}
