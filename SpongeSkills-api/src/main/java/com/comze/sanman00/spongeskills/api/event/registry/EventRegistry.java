package com.comze.sanman00.spongeskills.api.event.registry;

import com.comze.sanman00.spongeskills.api.event.EventBase;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A registry that keeps track of what event handlers are registered for certain
 * events.
 */
public interface EventRegistry {
    static final EventRegistry INSTANCE = null;
    /**
     * Registers the passed event handler to be called when the specified event
     * is fired.
     * 
     * @param <E> The event class which, when fired, causes the event handler to
     * be invoked
     * @param eventClass The event class
     * @param handler The event handler to be called
     */
    <E extends EventBase> void registerEventHandler(Class<E> eventClass, Consumer<? extends E> handler);
    
    /**
     * Removes the passed event handler so that it is not called when the 
     * specified event is fired. This mostly only works if the event handlers
     * cached (i.e. if they are stored in variables/fields). This method does
     * nothing if the specified event is not registered with this registry.
     * 
     * @param <E> The event class to remove this handler from
     * @param eventClass The event class
     * @param handler The event handler to be removed
     */
    <E extends EventBase> void removeEventHandler(Class<E> eventClass, Consumer<? extends E> handler);
    
    /**
     * Gets all of the event handlers registered for the passed event class.
     * 
     * @param <E> The event class whose handlers are to be retrieved
     * @param eventClass The event class
     * @return The handlers for this event, if any, otherwise an empty
     * collection
     */
    <E extends EventBase> Collection<Consumer<E>> getHandlersForEvent(Class<E> eventClass);
    
    /**
     * Gets all the handlers registered for all events.
     * 
     * @return All of the handlers registered for all events as an immutable map,
     * if any, otherwise an empty map.
     */
    Map<Class<? extends EventBase>, Collection<Consumer<? extends EventBase>>> getAllHandlers();
}
