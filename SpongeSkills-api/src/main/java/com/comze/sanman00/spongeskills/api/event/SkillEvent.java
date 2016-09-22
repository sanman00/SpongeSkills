package com.comze.sanman00.spongeskills.api.event;

import com.comze.sanman00.spongeskills.api.skill.Skill;
import java.util.Optional;

/**
 * Base interface for events that involve or affect a skill.
 */
public interface SkillEvent {
    /**
     * Gets the object that caused this event. The object could include almost 
     * anything; for example it could be the server instance, a plugin instance, 
     * an entity, a dedicated event cause object, etc.
     * 
     * @return The cause of this event
     */
    Optional<? extends Object> getCause();
    
    /**
     * Gets the skill that was involved or affected in this event.
     * 
     * @return The skill that was involved
     */
    Skill getSkill();
}
