package com.comze.sanman00.spongeskills.api.event;

import com.comze.sanman00.spongeskills.api.skill.Skill;

/**
 * Base interface for events that involve or affect a skill.
 */
public interface SkillEvent extends EventBase {
    /**
     * Gets the skill that was involved in or affected by this event.
     * 
     * @return The skill that was involved
     */
    Skill getSkill();
}
