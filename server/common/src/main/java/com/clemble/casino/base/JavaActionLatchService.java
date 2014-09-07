package com.clemble.casino.base;

import com.clemble.casino.ActionLatch;
import com.clemble.casino.event.PlayerAwareEvent;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mavarazy on 8/20/14.
 */
public class JavaActionLatchService implements ActionLatchService {

    final private ConcurrentHashMap<String, ActionLatch> LATCH_MAP = new ConcurrentHashMap<>();

    @Override
    public ActionLatch save(String key, ActionLatch latch) {
        return LATCH_MAP.putIfAbsent(key, latch);
    }

    @Override
    public ActionLatch get(String key) {
        return LATCH_MAP.get(key);
    }

    @Override
    public ActionLatch update(String key, PlayerAwareEvent event) {
        return LATCH_MAP.computeIfPresent(key, (k, v) -> v.put(event));
    }
}
