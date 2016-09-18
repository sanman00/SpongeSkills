package com.comze.sanman00.spongeskills.api.util;

import java.util.Arrays;
import java.util.Collection;

public final class CollectionUtil {
    private CollectionUtil() {
        
    }
    
    public static boolean containsAny(Collection<?> coll, Object[] array) {
        return containsAny(coll, Arrays.asList(array));
    }
    
    public static boolean containsAny(Collection<?> coll, Collection<?> coll2) {
        if (coll == null) {
            return coll2 == null;
        }
        return coll2.stream().anyMatch(coll::contains);
    }
}
