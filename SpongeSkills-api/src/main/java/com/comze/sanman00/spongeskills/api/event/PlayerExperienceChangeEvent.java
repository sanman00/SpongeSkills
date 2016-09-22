package com.comze.sanman00.spongeskills.api.event;

import com.comze.sanman00.spongeskills.api.player.PlayerWrapper;

/**
 * An event that is fired when a player's experience changes.
 * 
 * <p>This event may be fired when, for example, a command that gives the player 
 * some experience is executed, the player triggers a skill (which would fire a 
 * {@link SkillTriggerEvent}) and the skill gives experience as a result, the 
 * server automatically gives the player some experience, etc.</p>
 */
public interface PlayerExperienceChangeEvent extends SkillEvent {
    /**
     * Gets the player that was involved in this event.
     * 
     * @return The player that was involved in this event
     */
    PlayerWrapper<?> getPlayer();
    
    /**
     * Gets the player's experience before it changed and the event fired.
     * 
     * @return The player's experience before the event fired
     */
    int getExperienceBefore();
    
    /**
     * Gets the player's experience after it changed and the event fired.
     * 
     * @return The player's experience after the event fired
     */
    int getExperienceAfter();
}
