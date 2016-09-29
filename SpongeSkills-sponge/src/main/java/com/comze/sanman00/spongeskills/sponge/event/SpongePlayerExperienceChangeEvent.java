package com.comze.sanman00.spongeskills.sponge.event;

import com.comze.sanman00.spongeskills.api.event.PlayerExperienceChangeEvent;
import com.comze.sanman00.spongeskills.api.skill.Skill;
import com.comze.sanman00.spongeskills.sponge.player.SpongePlayerWrapper;
import org.spongepowered.api.event.cause.Cause;

public class SpongePlayerExperienceChangeEvent extends SpongeSkillEvent implements PlayerExperienceChangeEvent {
    public static class Builder {
        private Cause cause;
        private Skill skill;
        private SpongePlayerWrapper player;
        private int expBefore;
        private int expAfter;

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

        public Builder expBefore(int expBefore) {
            this.expBefore = expBefore;
            return this;
        }

        public Builder expAfter(int expAfter) {
            this.expAfter = expAfter;
            return this;
        }

        public SpongePlayerExperienceChangeEvent build() {
            return new SpongePlayerExperienceChangeEvent(this.cause, this.skill, this.player, this.expBefore, this.expAfter);
        }
    }

    public static SpongePlayerExperienceChangeEvent.Builder builder() {
        return new SpongePlayerExperienceChangeEvent.Builder();
    }

    private final SpongePlayerWrapper player;
    private final int expBefore;
    private final int expAfter;

    public SpongePlayerExperienceChangeEvent(Cause cause, Skill skill, SpongePlayerWrapper player, int expBefore, int expAfter) {
        super(cause, skill);
        this.player = player;
        this.expBefore = expBefore;
        this.expAfter = expAfter;
    }

    @Override
    public SpongePlayerWrapper getPlayer() {
        return this.player;
    }

    @Override
    public int getExperienceAfter() {
        return this.expAfter;
    }

    @Override
    public int getExperienceBefore() {
        return this.expBefore;
    }
}
