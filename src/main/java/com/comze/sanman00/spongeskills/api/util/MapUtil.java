package com.comze.sanman00.spongeskills.api.util;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class MapUtil {
    private MapUtil() {
        
    }
    
    public static <K, V> Map<K, V> newMap() {
        return new HashMap<>();
    }
    
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> newMap(Object... objects) {
        checkArgument(objects.length % 2 == 0, "Expected array size to be even!");
        Map<K, V> map = new HashMap<>();
        Iterator<Object> it = Arrays.asList(objects).iterator();
        
        while (it.hasNext()) {
            K key = (K) it.next();
            V value = (V) it.next();
            
            map.put(key, value);
        }
        return map;
    }
}
