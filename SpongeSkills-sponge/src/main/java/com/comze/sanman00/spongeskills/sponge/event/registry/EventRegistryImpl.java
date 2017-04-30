package com.comze.sanman00.spongeskills.sponge.event.registry;

import com.comze.sanman00.spongeskills.api.event.EventBase;
import com.comze.sanman00.spongeskills.api.event.registry.EventRegistry;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Consumer;

public class EventRegistryImpl implements EventRegistry {
    private final Map<Class<? extends EventBase>, Collection<Consumer<? extends EventBase>>> eventHandlers = new HashMap<>();
    public static final EventRegistryImpl IMPL_INSTANCE = new EventRegistryImpl();
    
    EventRegistryImpl() {
        try {
            Field instance = EventRegistry.class.getDeclaredField("INSTANCE");
            Field modifiers = Field.class.getDeclaredField("modifiers");
            Field.setAccessible(new Field[] {instance, modifiers}, true);
            modifiers.setInt(modifiers, instance.getModifiers() & ~Modifier.FINAL);
            instance.set(null, this);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Map<Class<? extends EventBase>, Collection<Consumer<? extends EventBase>>> getAllHandlers() {
        return ImmutableMap.copyOf(this.eventHandlers);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E extends EventBase> Collection<Consumer<E>> getHandlersForEvent(Class<E> eventClass) {
       return ImmutableSet.<Consumer<E>>builder().addAll((Collection) this.eventHandlers.get(eventClass)).build();
    }

    @Override
    public <E extends EventBase> void registerEventHandler(Class<E> eventClass, Consumer<? extends E> handler) {
        if (!this.eventHandlers.containsKey(eventClass)) {
            this.eventHandlers.put(eventClass, new HashSet<>());
        }
        this.eventHandlers.get(eventClass).add(handler);
    }

    @Override
    public <E extends EventBase> void removeEventHandler(Class<E> eventClass, Consumer<? extends E> handler) {
        if (!this.eventHandlers.containsKey(eventClass)) {
            return;
        }
        this.eventHandlers.get(eventClass).remove(handler);
    }
}
