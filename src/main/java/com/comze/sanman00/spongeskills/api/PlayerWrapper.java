package com.comze.sanman00.spongeskills.api;

/**
 * A wrapper for a player instance. This class is to be used by the implementation
 * and API consumers.
 * 
 * @param <T> The Java type of the player
 */
public final class PlayerWrapper<T> {
    /**
     * The wrapped player.
     */
    private final T player;

    /**
     * Creates a new wrapper with the specified player.
     * 
     * @param player The player to be wrapped
     */
    public PlayerWrapper(T player) {
        this.player = player;
    }
    
    /**
     * Gets the player that was wrapped.
     * 
     * @return The player that was wrapped
     */
    public T getPlayer() {
        return this.player;
    }
}
