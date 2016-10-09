package com.comze.sanman00.spongeskills.api.skill;

/**
 * Represents a skill.
 * 
 * <p>This interface represents the informational aspects of a
 * skill (such as its ID and its name) and so is not tied to a specific player; 
 * it only describes the skill.</p>
 * 
 * <p>If a plugin, a mod or the implementation adds other skills then they are 
 * responsible for exposing those skills.</p>
 * 
 * @implSpec The implementation of this interface should be immutable
 */
public interface Skill {
    /**
     * Gets the ID of this skill.
     * 
     * @return The skill's ID
     */
    String getId();
    
    /**
     * Gets the name of this skill.
     * 
     * @return The skill's name
     */
    String getName();
}
