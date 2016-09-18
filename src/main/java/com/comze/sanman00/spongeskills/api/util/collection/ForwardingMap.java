package com.comze.sanman00.spongeskills.api.util.collection;

import java.util.Map;

public interface ForwardingMap<K, V> extends Map<K, V> {
    Map<K, V> getContainedMap();
}
