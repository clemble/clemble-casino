package com.clemble.casino.goal.listener;

import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.repository.ShortGoalStateRepository;
import com.clemble.casino.server.event.goal.SystemGoalForbidBetEvent;
import com.clemble.casino.server.event.goal.SystemGoalStartedEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;

/**
 * Created by mavarazy on 1/13/15.
 */
public class SystemGoalForbidBetEventListener implements SystemEventListener<SystemGoalForbidBetEvent> {

    final private ShortGoalStateRepository stateRepository;

    public SystemGoalForbidBetEventListener(ShortGoalStateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    @Override
    public void onEvent(SystemGoalForbidBetEvent event) {
        // Step 1. Fetching state
        GoalState state = stateRepository.findOne(event.getGoalKey());
        // Step 2. Checking state
        if (state.getBetsAllowed()) {
            stateRepository.save(state.forbidBet());
        }
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
