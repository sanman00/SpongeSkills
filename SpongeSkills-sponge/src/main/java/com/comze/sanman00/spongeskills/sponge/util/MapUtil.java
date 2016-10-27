package com.comze.sanman00.spongeskills.sponge.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class MapUtil {
    private MapUtil() {
        
    }
    
    public static <K, V> Map<K, V> zip(Set<K> keys, Set<V> values) {
        Iterator<K> keysIterator = keys.iterator();
        Iterator<V> valuesIterator = values.iterator();
        
        Map<K, V> map = new HashMap<>();
        while (keysIterator.hasNext() || valuesIterator.hasNext()) {
            map.put(keysIterator.next(), valuesIterator.next());
        }
        
        return map;
    }
    
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> zip(Object... objects) {
        if (objects.length % 2 != 0) {
            throw new IllegalArgumentException("Array size must be even!");
        }
        Map<K, V> map = new HashMap<>();
        
        for (int x = 0;x < objects.length;x += 2) {
            map.put((K) objects[x], (V) objects[x + 1]);
        }
        return map;
    }
}
