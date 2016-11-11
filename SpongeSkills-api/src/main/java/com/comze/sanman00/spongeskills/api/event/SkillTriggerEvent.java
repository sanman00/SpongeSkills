package com.comze.sanman00.spongeskills.api.event;

import com.comze.sanman00.spongeskills.api.player.PlayerWrapper;

/**
 * An event that is fired when a skill is triggered.
 */
public interface SkillTriggerEvent extends SkillEvent {
    /**
     * Gets the player that was involved in the event.
     * 
     * @return The player involved in the event
     */
    PlayerWrapper<?> getPlayer();
}
