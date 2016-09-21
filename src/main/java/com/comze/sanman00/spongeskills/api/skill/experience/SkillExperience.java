package com.comze.sanman00.spongeskills.api.skill.experience;

import com.comze.sanman00.spongeskills.api.player.PlayerWrapper;
import com.comze.sanman00.spongeskills.api.skill.Skill;

/**
 * Represents the experience of a skill. Instances of this class are per-player; 
 * they do not represent a world, the server, etc.
 */
public interface SkillExperience {
    /**
     * Gets the total experience of this skill as an integer.
     * 
     * @return The experience as an integer
     */
    int getTotalExperience();
    
    /**
     * Gets the experience that is remaining until the player levels up.
     * 
     * @return The remaining experience
     */
    int getRemainingExperience();
    
    /**
     * Gets the level of this skill.
     * 
     * @return The level of this skill
     */
    int getLevel();
    
    /**
     * Gets the skill that this experience is relevant to.
     * 
     * @return The skill itself
     */
    Skill getSkill();
    
    /**
     * Gets the player that this experience belongs to.
     * 
     * @return The player
     */
    PlayerWrapper<?> getPlayer();
}
