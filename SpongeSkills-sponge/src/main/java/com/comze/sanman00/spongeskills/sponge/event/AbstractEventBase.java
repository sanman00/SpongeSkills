package com.comze.sanman00.spongeskills.sponge.event;

import com.comze.sanman00.spongeskills.api.event.EventBase;
import java.util.Optional;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.cause.Cause;

public abstract class AbstractEventBase implements EventBase, Event {
    private final Cause cause;
    
    public AbstractEventBase(Cause cause) {
        this.cause = cause;
    }
    
    @Override
    public Optional<? extends Object> getEventCause() {
        return Optional.of(getCause());
    }

    @Override
    public Cause getCause() {
        return this.cause;
    }
}
