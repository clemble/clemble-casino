package com.gogomaya.server.player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class PlayerAwareUtils {

    private PlayerAwareUtils() {
        throw new IllegalAccessError();
    }

    public static <M extends PlayerAware> Map<Long, M> toMap(Collection<? extends M> sourceCollection) {
        // Step 0. Sanity check
        if (sourceCollection == null || sourceCollection.isEmpty())
            return ImmutableMap.<Long, M> of();
        // Step 1. Converting to Map
        HashMap<Long, M> tmpMap = new HashMap<Long, M>();
        for (M value : sourceCollection) {
            if (value != null)
                tmpMap.put(value.getPlayerId(), value);
        }
        // Step 2. Creating immutable map from tmp map
        return tmpMap;
    }
    
    public static <M extends PlayerAware> ImmutableMap<Long, M> toImmutableMap(Collection<? extends M> sourceCollection) {
        return ImmutableMap.<Long, M>copyOf(toMap(sourceCollection));
    }

}
