package com.terminalvelocitycabbage.engine.utils;

import java.util.Objects;

public record Pair<K, V>(K key, V value) {

    @Override
    public boolean equals(Object o) {
        return o instanceof Pair<?, ?> pair && Objects.equals(key, pair.key) && Objects.equals(value, pair.value);
    }

    @Override
    public String toString() {
        return "Pair[key: " + key + ", value: " + value + "]";
    }
}
