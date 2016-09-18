package com.comze.sanman00.spongeskills.api.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public final class ObjectUtil {
    private ObjectUtil() {
        
    }
    
    /**
     * Checks if the given object matches a map of given attributes.
     * <br />
     * How to use:
     * <pre><code>
     *      public boolean equals(Object obj) {
     *          return ObjectUtil.equals(this, obj, t -> MapUtil.newMap(t.myAttributeA, this.myAttributeA, t.myAttributeB, this.myAttributeB));
     *      }
     * </code></pre>
     * @param <T> The type of the caller and the object to check
     * @param thisInstance The 'this' keyword instance
     * @param objToCheck The object to check
     * @param mapFromObjToCheck The map of attributes with which to test the object
     * @return Whether the object to check matches the given map of attributes
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean equals(T thisInstance, Object objToCheck, Function<T, Map<?, ?>> mapFromObjToCheck) {
        if (objToCheck == null || !objToCheck.getClass().isInstance(expectNonNull(thisInstance, "'this' instance is supposed to be non-null!"))) {
            return false;
        }
        
        if (objToCheck == thisInstance) {
            return true;
        }
        
        return mapFromObjToCheck.apply((T) objToCheck).entrySet().stream().allMatch(entry -> Objects.equals(entry.getKey(), entry.getValue()));
    }
    
    public static <T> int hashCode(int hash, int seed, Object... objectsToBeHashed) {
        int finalHash = hash;
        
        for (Object obj : objectsToBeHashed) {
            finalHash = seed * finalHash + Objects.hashCode(obj);
        }
        return finalHash;
    }
    
    public static <T> T expectNonNull(T object, Throwable throwable) {
        if (object == null) {
            throwAsUnchecked(throwable);
        }
        
        return object;
    }
    
    public static <T> T expectNonNull(T object, String errorMessage) {
        return expectNonNull(object, new IllegalArgumentException(errorMessage));
    }
    
    public static <T> T expectNonNull(T object) {
        return expectNonNull(object, new IllegalArgumentException());
    }
    
    public static void expectThat(boolean expr, Throwable throwable) {
        if (!expr) {
            throwAsUnchecked(throwable);
        }
    }
    
    public static void expectThat(boolean expr, String errorMessage) {
        expectThat(expr, new IllegalArgumentException(errorMessage));
    }
    
    public static void expectThat(boolean expr) {
        expectThat(expr, new IllegalArgumentException());
    }
    
    public static void throwAsUnchecked(Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            throw (RuntimeException) throwable;
        }
        
        else if (throwable instanceof Error) {
            throw (Error) throwable;
        }
        
        else {
            throw new RuntimeException(throwable);
        }
    }
    
    public static void setStaticFinalField(Field field, Object value) {
        field.setAccessible(true);
        try {
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(null, value);
        }
        catch (NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }
    
    public <T> Optional<T> getOrEmpty(T[] array, int index) {
        return getOrEmpty(Arrays.asList(array), index);
    }
    
    public <T> Optional<T> getOrEmpty(List<T> list, int index) {
        try {
            return Optional.ofNullable(list.get(index));
        }
        catch (IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }
}
