package com.clemble.casino.base;

import com.clemble.casino.ActionLatch;
import com.clemble.casino.event.PlayerAwareEvent;

/**
 * Created by mavarazy on 8/20/14.
 */
public interface ActionLatchService {

    ActionLatch save(String key, ActionLatch latch);

    ActionLatch get(String key);

    ActionLatch update(String key, PlayerAwareEvent event);

}
