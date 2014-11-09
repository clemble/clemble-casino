package com.clemble.casino.goal.listener;

import com.clemble.casino.ImmutablePair;
import com.clemble.casino.goal.action.GoalManagerFactoryFacade;
import com.clemble.casino.goal.event.GoalEvent;
import com.clemble.casino.goal.lifecycle.management.GoalContext;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.lifecycle.configuration.rule.time.PlayerClock;
import com.clemble.casino.lifecycle.management.PlayerContext;
import com.clemble.casino.lifecycle.management.event.action.PlayerAction;
import com.clemble.casino.server.action.ClembleManager;
import com.clemble.casino.server.event.goal.SystemGoalTimeoutEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;

import java.util.*;
import java.util.Map.Entry;

/**
 * Created by mavarazy on 11/8/14.
 */
public class SystemGoalTimeoutEventListener implements SystemEventListener<SystemGoalTimeoutEvent> {

    final private GoalManagerFactoryFacade managerFactory;

    final private Comparator<Entry<Long, PlayerAction>> COMPARATOR = new Comparator<Entry<Long, PlayerAction>>() {
        @Override
        public int compare(Entry<Long, PlayerAction> o1, Entry<Long, PlayerAction> o2) {
            return o1.getKey().compareTo(o2.getKey());
        }
    };

    public SystemGoalTimeoutEventListener(GoalManagerFactoryFacade managerFactory) {
        this.managerFactory = managerFactory;
    }

    @Override
    public void onEvent(SystemGoalTimeoutEvent event) {
        // Step 1. Fetching related GameState
        ClembleManager<GoalEvent, GoalState> manager = managerFactory.get(event.getGoalKey());
        // Step 2. Extracting game context
        GoalContext context = manager.getState().getContext();
        // Step 3. Processing all breach events
        List<Entry<Long, PlayerAction>> actions = new ArrayList<Entry<Long, PlayerAction>>();
        for(PlayerContext playerContext: context.getPlayerContexts()) {
            // Step 3.1. Going through each player context and calling expired events
            PlayerClock clock = playerContext.getClock();
            if (clock.wasBreached()) {
                // Step 3.2. Generating player action
                PlayerAction action = new PlayerAction(event.getGoalKey(), playerContext.getPlayer(), clock.getPunishment().toBreachEvent());
                // Step 3.3. Adding new entry to Actions
                actions.add(new ImmutablePair<>(clock.getBreachTime(), action));
            }
        }
        // Step 4.0. Sanity check
        if(actions.isEmpty())
            return;
        // Step 4. Sorting events
        Collections.sort(actions, COMPARATOR);
        // Step 5. Processing actions in order
        for(Entry<Long, PlayerAction> action: actions) {
            manager.process(action.getValue());
        }
    }

    @Override
    public String getChannel() {
        return SystemGoalTimeoutEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemGoalTimeoutEvent.CHANNEL + " > game:management";
    }
}
