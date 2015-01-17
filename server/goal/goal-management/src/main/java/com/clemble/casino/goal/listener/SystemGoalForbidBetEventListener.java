package com.clemble.casino.goal.listener;

import com.clemble.casino.ImmutablePair;
import com.clemble.casino.event.Event;
import com.clemble.casino.goal.action.GoalManagerFactory;
import com.clemble.casino.goal.action.GoalManagerFactoryFacade;
import com.clemble.casino.goal.event.GoalEvent;
import com.clemble.casino.goal.lifecycle.management.GoalContext;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.repository.GoalStateRepository;
import com.clemble.casino.lifecycle.configuration.rule.time.PlayerClock;
import com.clemble.casino.lifecycle.management.PlayerContext;
import com.clemble.casino.lifecycle.management.event.action.PlayerAction;
import com.clemble.casino.lifecycle.management.event.action.bet.BetOffAction;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.action.ClembleManager;
import com.clemble.casino.server.action.ClembleManagerFactory;
import com.clemble.casino.server.event.goal.SystemGoalForbidBetEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by mavarazy on 1/13/15.
 */
public class SystemGoalForbidBetEventListener implements SystemEventListener<SystemGoalForbidBetEvent> {

    final private GoalManagerFactoryFacade managerFactory;

    public SystemGoalForbidBetEventListener(GoalManagerFactoryFacade managerFactory) {
        this.managerFactory = managerFactory;
    }

    @Override
    public void onEvent(SystemGoalForbidBetEvent event) {
        // Step 1. Fetching related GameManager
        ClembleManager<GoalEvent, ? extends GoalState> manager = managerFactory.get(event.getGoalKey());
        // Step 2. Creating bet off action
        PlayerAction betOffAction = new PlayerAction<BetOffAction>(event.getGoalKey(), PlayerAware.DEFAULT_PLAYER, BetOffAction.INSTANCE);
        // Step 3. Processing bet off action
        manager.process(betOffAction);
    }

    @Override
    public String getChannel() {
        return SystemGoalForbidBetEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemGoalForbidBetEvent.CHANNEL + "> goal:management";
    }
}
