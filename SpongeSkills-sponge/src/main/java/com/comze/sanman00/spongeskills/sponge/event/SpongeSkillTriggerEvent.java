package com.comze.sanman00.spongeskills.sponge.event;

import com.comze.sanman00.spongeskills.api.event.SkillTriggerEvent;
import com.comze.sanman00.spongeskills.api.skill.Skill;
import org.spongepowered.api.event.cause.Cause;

public class SpongeSkillTriggerEvent extends SpongeSkillEvent implements SkillTriggerEvent {
    public static class Builder {
        private Cause cause;
        private Skill skill;

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

        public SpongeSkillTriggerEvent build() {
            return new SpongeSkillTriggerEvent(this.cause, this.skill);
        }
    }

    public static SpongeSkillTriggerEvent.Builder builder() {
        return new SpongeSkillTriggerEvent.Builder();
    }

    public SpongeSkillTriggerEvent(Cause cause, Skill skill) {
        super(cause, skill);
    }
}
