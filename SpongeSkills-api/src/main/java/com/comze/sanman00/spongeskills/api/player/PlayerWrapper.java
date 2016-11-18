package com.comze.sanman00.spongeskills.api.player;

import com.comze.sanman00.spongeskills.api.skill.Skill;
import com.comze.sanman00.spongeskills.api.skill.experience.SkillExperience;
import java.util.Map;
import java.util.UUID;

/**
 * A wrapper for a player instance.
 * 
 * <p>This class is to be used by the implementation and API consumers to wrap 
 * the Player class or interface, if it exists, that their API, if any, provides.
 * If there are multiple Player types being used, then instances of different 
 * {@code PlayerWrapper} implementations should be used to represent them.</p>
 * 
 * @param <T> The Java type of the player from the underlying implementation or 
 * API, if any
 */
public interface PlayerWrapper<T> {
    /**
     * Gets the player instance that was wrapped.
     * 
     * @return The player that was wrapped
     */
    T getWrappedPlayer();
    
    /**
     * Levels up the player by a single level. This may increase the amount 
     * of experience the player has.
     * 
     * @param skill The skill to level up
     */
    default void levelUp(Skill skill) {
        levelUp(1, skill);
    }
    
    /**
     * Levels up the player by a specified amount. This may increase the amount 
     * of experience the player has.
     * 
     * @param amount The number of levels to level up the player by
     * @param skill The skill to level up
     */
    void levelUp(int amount, Skill skill);
    
    /**
     * Gives the player a certain amount of experience. This may cause the player
     * to level up.
     * 
     * @param amount The amount of experience to give
     * @param skill The skill to give experience to
     */
    void giveExperience(int amount, Skill skill);
    
    /**
     * Gets all the experience the player has. No updates are made to the map
     * once this method returns (i.e. this is not a live view of the map), therefore, 
     * the map should be immutable to ensure this. The map must not be {@code null}.
     * 
     * @return A {@code Map} of the {@link SkillExperience} instances that belong to 
     * this player
     */
    Map<Skill, SkillExperience> getExperience();
    
    /**
     * Gets the experience the player has for the specified skill.
     * 
     * @param skill Skill to get experience for
     * @return The experience the player has for the specified skill
     */
    default SkillExperience getExperience(Skill skill) {
        return getExperience().get(skill);
    }
    
    /**
     * Gets the UUID of the player represented by this wrapper.
     * 
     * @return The UUID of the player represented by this wrapper
     */
    UUID getPlayerUUID();
}
