package com.comze.sanman00.spongeskills.api.util.collection;

import com.google.common.collect.ImmutableMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public abstract class AbstractForwardingMap<K, V> implements ForwardingMap<K, V> {
    static class Impl<K, V> extends AbstractForwardingMap<K, V> {
        private final BiConsumer<K, V> onEntryPutFunc;
        private final BiConsumer<K, V> onEntryRemoveFunc;
        
        Impl(Map<K, V> map, BiConsumer<K, V> onEntryPutFunc, BiConsumer<K, V> onEntryRemoveFunc) {
            super(map);
            this.onEntryPutFunc = onEntryPutFunc;
            this.onEntryRemoveFunc = onEntryRemoveFunc;
        }

        @Override
        void onEntryPut(K key, V value) {
            if (this.onEntryPutFunc != null) {
                this.onEntryPutFunc.accept(key, value);
            }
        }

        @Override
        void onEntryRemove(K key, V removedValue) {
            if (this.onEntryRemoveFunc != null) {
                this.onEntryRemoveFunc.accept(key, removedValue);
            }
        }
    }
    private final Map<K, V> map;

    protected AbstractForwardingMap(Map<K, V> map) {
        this.map = map;
    }
    
    static <K, V> ForwardingMap<K, V> create(Map<K, V> map, BiConsumer<K, V> onEntryPutFunc, BiConsumer<K, V> onEntryRemoveFunc) {
        return new Impl<>(map, onEntryPutFunc, onEntryRemoveFunc);
    }
    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return this.map.get(key);
    }

    @Override
    public V put(K key, V value) {
        return this.map.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return this.map.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        this.map.putAll(m);
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    @Override
    public Set<K> keySet() {
        return this.map.keySet();
    }

    @Override
    public Collection<V> values() {
        return this.map.values();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return this.map.entrySet();
    }

    /**
     * Returns an immutable copy of the map contained in this instance of 
     * {@code ForwardingEventMap}. The returned map is immutable.
     * @return An immutable copy of the map contained in this instance
     */
    public Map<K, V> getContainedMap() {
        return ImmutableMap.copyOf(this.map);
    }
    
    abstract void onEntryPut(K key, V value);
    
    abstract void onEntryRemove(K key, V removedValue);
}
