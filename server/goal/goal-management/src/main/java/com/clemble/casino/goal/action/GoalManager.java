package com.clemble.casino.goal.action;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.event.Event;
import com.clemble.casino.goal.aspect.GoalAspect;
import com.clemble.casino.goal.event.GoalEvent;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.lifecycle.management.Manager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Created by mavarazy on 10/8/14.
 */
public class GoalManager implements Manager {

    final private static Logger LOG = LoggerFactory.getLogger(GoalManager.class);

    final private GoalState state;
    final private Collection<GoalAspect<?>> aspects;

    public GoalManager(GoalState state, Collection<GoalAspect<?>> aspects) {
        this.state = state;
        this.aspects = aspects;
    }

    @Override
    public GoalState getState() {
        return state;
    }

    @Override
    public Event process(Event action) {
        // Step 1. Sanity check
        // Step 1.1 Add check for ended games check
        // Step 2. Acquiring lock for the session, to exclude parallel processing
        LOG.debug("Processing {}", action);
        // Step 1. Before move notification
        for (GoalAspect aspect : aspects)
            aspect.onEvent(action);
        // Step 2. Processing in core
        GoalEvent event = state.process(action);
        // Step 3 After move notification
        for (GoalAspect aspect : aspects)
            aspect.onEvent(event);
        // Step 3. Returning game event
        return event;
    }

}
