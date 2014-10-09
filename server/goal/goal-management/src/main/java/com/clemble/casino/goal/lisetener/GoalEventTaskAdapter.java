package com.clemble.casino.goal.lisetener;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.lifecycle.management.event.GameManagementEvent;
import com.clemble.casino.goal.action.GoalManagerFactoryFacade;
import com.clemble.casino.goal.event.GoalEvent;
import com.clemble.casino.server.action.ClembleManager;
import com.clemble.casino.server.aspect.time.PlayerTimeTask;
import com.clemble.casino.server.executor.EventTask;
import com.clemble.casino.server.executor.EventTaskAdapter;

import java.util.Collection;

/**
 * Created by mavarazy on 10/9/14.
 */
public class GoalEventTaskAdapter implements EventTaskAdapter {

    final private GoalManagerFactoryFacade managerFactory;

    public GoalEventTaskAdapter(GoalManagerFactoryFacade managerFactory) {
        this.managerFactory = managerFactory;
    }

    @Override
    public void process(EventTask task) {
                // Step 1. Processing task
        Collection<? extends Event> events = task.execute();
        // Step 1.1 If there is no events nothing much to do return
        if(events.isEmpty())
            return;
        if(task instanceof PlayerTimeTask) {
            // Step 2.1 Fetching session key
            String sessionKey = task.getKey();
            // Step 2.1.1 Fetching manager
            ClembleManager<GoalEvent, ?> manager = managerFactory.get(sessionKey);
            // Step 2.1.2 Going event by event and processing
            events.forEach(event -> manager.process(event));
        } else {
            throw new IllegalAccessError();
        }
    }

}
