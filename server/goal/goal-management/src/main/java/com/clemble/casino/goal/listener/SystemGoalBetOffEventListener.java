package com.clemble.casino.goal.listener;

import com.clemble.casino.goal.action.GoalManagerFactoryFacade;
import com.clemble.casino.goal.event.GoalEvent;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.lifecycle.management.event.action.PlayerAction;
import com.clemble.casino.lifecycle.management.event.action.bet.BetOffAction;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.action.ClembleManager;
import com.clemble.casino.server.event.goal.SystemGoalBetOffEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;

/**
 * Created by mavarazy on 1/13/15.
 */
public class SystemGoalBetOffEventListener implements SystemEventListener<SystemGoalBetOffEvent> {

    final private GoalManagerFactoryFacade managerFactory;

    public SystemGoalBetOffEventListener(GoalManagerFactoryFacade managerFactory) {
        this.managerFactory = managerFactory;
    }

    @Override
    public void onEvent(SystemGoalBetOffEvent event) {
        // Step 1. Fetching related GameManager
        ClembleManager<GoalEvent, ? extends GoalState> manager = managerFactory.get(event.getGoalKey());
        // Step 2. Creating bet off action
        PlayerAction betOffAction = new PlayerAction<BetOffAction>(event.getGoalKey(), PlayerAware.DEFAULT_PLAYER, BetOffAction.INSTANCE);
        // Step 3. Processing bet off action
        manager.process(betOffAction);
    }

    @Override
    public String getChannel() {
        return SystemGoalBetOffEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemGoalBetOffEvent.CHANNEL + "> goal:management";
    }
}
