package com.clemble.casino.server.game.action;

import com.clemble.casino.event.Event;
import com.clemble.casino.server.executor.EventTaskAdapter;

import java.util.Collection;

/**
 * Created by mavarazy on 9/9/14.
 */
public class GameEventTaskAdapter implements EventTaskAdapter {

    final private GameManagerFactory managerFactory;

    public GameEventTaskAdapter(GameManagerFactory managerFactory) {
        this.managerFactory = managerFactory;
    }


    @Override
    public void process(String sessionKey, Collection<Event> events) {
        // Step 1. Fetching manager
        GameManager<?> manager = this.managerFactory.get(sessionKey);
        // Step 2. Going event by event and processing
        events.forEach(event -> manager.process(event));
    }

}
