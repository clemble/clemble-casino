package com.gogomaya.player;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PlayerAwareUtils {

    private PlayerAwareUtils() {
        throw new IllegalAccessError();
    }

    public static <M extends PlayerAware> Map<Long, M> toMap(Collection<? extends M> sourceCollection) {
        // Step 0. Sanity check
        if (sourceCollection == null || sourceCollection.isEmpty())
            return Collections.emptyMap();
        // Step 1. Converting to Map
        HashMap<Long, M> tmpMap = new HashMap<Long, M>();
        for (M value : sourceCollection) {
            if (value != null)
                tmpMap.put(value.getPlayerId(), value);
        }
        // Step 2. Creating immutable map from tmp map
        return tmpMap;
    }
    
    public static <M extends PlayerAware> Map<Long, M> toImmutableMap(Collection<? extends M> sourceCollection) {
        return Collections.unmodifiableMap(toMap(sourceCollection));
    }

}
