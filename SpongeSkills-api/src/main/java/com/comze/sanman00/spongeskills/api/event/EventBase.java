package com.comze.sanman00.spongeskills.api.event;

import java.util.Optional;

/**
 * A marker interface that is the superinterface of all events defined by this
 * plugin.
 */
public interface EventBase {
    /**
     * Gets the object that caused this event. The object could include almost 
     * anything; for example it could be the server instance, a plugin instance, 
     * an entity, a dedicated event cause object, etc.
     * 
     * @return The cause of this event
     */
    Optional<? extends Object> getEventCause();
}
