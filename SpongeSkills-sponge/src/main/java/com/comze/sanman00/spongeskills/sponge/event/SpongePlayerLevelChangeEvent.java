package com.comze.sanman00.spongeskills.sponge.event;

import com.comze.sanman00.spongeskills.api.event.PlayerLevelChangeEvent;
import com.comze.sanman00.spongeskills.api.skill.Skill;
import com.comze.sanman00.spongeskills.sponge.player.SpongePlayerWrapper;
import org.spongepowered.api.event.cause.Cause;

public class SpongePlayerLevelChangeEvent extends SpongeSkillEvent implements PlayerLevelChangeEvent {
    public static class Builder {
        private SpongePlayerWrapper player;
        private int levelBefore;
        private int levelAfter;
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

        public Builder player(SpongePlayerWrapper player) {
            this.player = player;
            return this;
        }

        public Builder levelBefore(int level) {
            this.levelBefore = level;
            return this;
        }

        public Builder levelAfter(int level) {
            this.levelAfter = level;
            return this;
        }

        public SpongePlayerLevelChangeEvent build() {
            return new SpongePlayerLevelChangeEvent(this.cause, this.skill, this.player, this.levelBefore, this.levelAfter);
        }
    }

    public static SpongePlayerLevelChangeEvent.Builder builder() {
        return new SpongePlayerLevelChangeEvent.Builder();
    }

    private final SpongePlayerWrapper player;
    private final int levelBefore;
    private final int levelAfter;

    public SpongePlayerLevelChangeEvent(Cause cause, Skill skill, SpongePlayerWrapper player, int levelBefore, int levelAfter) {
        super(cause, skill);
        this.player = player;
        this.levelBefore = levelBefore;
        this.levelAfter = levelAfter;
    }

    @Override
    public SpongePlayerWrapper getPlayer() {
        return this.player;
    }

    @Override
    public int getLevelBefore() {
        return this.levelBefore;
    }

    @Override
    public int getLevelAfter() {
        return this.levelAfter;
    }

    @Override
    public int getDifference() {
        return this.levelAfter - this.levelBefore;
    }
}
