package com.comze.sanman00.spongeskills.api.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class ToStringChain {
    private boolean finished;
    private final Map<String, String> objects;
    private final String className;

    public static ToStringChain create(Class<?> clazz) {
        return new ToStringChain(Objects.requireNonNull(clazz, "Passed class was null"));
    }

    public static ToStringChain create(Object instance) {
        return create(Objects.requireNonNull(instance, "Passed instance was null").getClass());
    }
    
    private ToStringChain(Class<?> clazz) {
        this.objects = new HashMap<>();
        this.className = clazz.getSimpleName();
    }

    public ToStringChain add(String name, String str) {
        if (this.finished) {
            throw new IllegalStateException("Cannot add elements to a finished chain");
        }
        this.objects.put(Objects.requireNonNull(name, "name"), str);
        return this;
    }

    public ToStringChain add(String name, Object obj) {
        add(name, String.valueOf(obj));
        return this;
    }

    public ToStringChain add(String name, Object[] obj) {
        add(name, Arrays.deepToString(obj));
        return this;
    }

    public ToStringChain add(String name, int[] obj) {
        add(name, Arrays.toString(obj));
        return this;
    }

    public ToStringChain add(String name, boolean[] obj) {
        add(name, Arrays.toString(obj));
        return this;
    }

    public ToStringChain add(String name, short[] obj) {
        add(name, Arrays.toString(obj));
        return this;
    }

    public ToStringChain add(String name, byte[] obj) {
        add(name, Arrays.toString(obj));
        return this;
    }

    public ToStringChain add(String name, char[] obj) {
        add(name, Arrays.toString(obj));
        return this;
    }

    public ToStringChain add(String name, double[] obj) {
        add(name, Arrays.toString(obj));
        return this;
    }

    public ToStringChain add(String name, float[] obj) {
        add(name, Arrays.toString(obj));
        return this;
    }

    public ToStringChain add(String name, long[] obj) {
        add(name, Arrays.toString(obj));
        return this;
    }

    public String finish() {
        StringBuilder sb = new StringBuilder();
        sb.append(className).append("{");
        
        ArrayList<String> keys = new ArrayList<>(this.objects.keySet());
        keys.forEach(name -> {
            if (keys.indexOf(name) < keys.size() - 1) {
                sb.append(name).append("=").append(this.objects.get(name)).append(", ");
            }
            
            else {
                sb.append(name).append("=").append(this.objects.get(name)).append("}");
            }
        });
        this.finished = true;
        return sb.toString();
    }

    @Override
    public String toString() {
        return this.finish();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        
        if (obj == null) {
            return false;
        }
        
        if (!(obj instanceof ToStringChain)) {
            return false;
        }
        
        ToStringChain toStr = (ToStringChain) obj;
        
        return Objects.equals(this.className, toStr.className)
            && Objects.equals(this.objects, toStr.objects);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.objects);
        hash = 67 * hash + Objects.hashCode(this.className);
        return hash;
    }
}
