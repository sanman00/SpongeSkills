package com.comze.sanman00.spongeskills.api.util.collection;

import java.util.Map;
import java.util.function.BiConsumer;

public final class ForwardingMaps {
    private ForwardingMaps() {
        
    }
    
    public static <K, V> ForwardingMap<K, V> create(Map<K, V> map, BiConsumer<K, V> onEntryPutFunc, BiConsumer<K, V> onEntryRemoveFunc) {
        return AbstractForwardingMap.create(map, onEntryPutFunc, onEntryRemoveFunc);
    }
}
