package com.comze.sanman00.spongeskills.api.event;

import com.comze.sanman00.spongeskills.api.player.PlayerWrapper;

/**
 * An event that is fired when the player's level changes. If the player's level 
 * increases by only one then {@link PlayerLevelChangeEvent#isLevelUp()} may be 
 * true.
 */
public interface PlayerLevelChangeEvent extends SkillEvent {
    /**
     * Gets the player who leveled up. The player returned by this method may not 
     * be this event's cause (if it has one).
     * 
     * @return The player involved in this event
     */
    PlayerWrapper<?> getPlayer();
    
    /**
     * Gets the player's level before they leveled up and the event fired.
     * 
     * @return The player's level before the event fired
     */
    int getLevelBefore();
    
    /**
     * Gets the player's level after they leveled up and the event fired.
     * 
     * @return The player's level after the event fired
     */
    int getLevelAfter();
    
    /**
     * Gets the difference between the previous level and the new level.
     * 
     * @return The difference between the current level and the previous level, which
     * may be negative
     */
    int getDifference();
}
