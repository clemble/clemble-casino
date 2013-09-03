package com.gogomaya.client;

import java.util.Map.Entry;

public class ImmutablePair<K, V> implements Entry<K, V> {

    final private K key;
    final private V value;

    public ImmutablePair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        throw new UnsupportedOperationException();
    }

}
