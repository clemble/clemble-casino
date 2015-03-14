package com.clemble.casino.goal.listener;

import com.clemble.casino.goal.lifecycle.management.GoalVictory;
import com.clemble.casino.goal.repository.GoalVictoryRepository;
import com.clemble.casino.server.event.goal.SystemGoalReachedEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;

import javax.validation.Valid;

/**
 * Created by mavarazy on 3/14/15.
 */
public class SystemGoalReachedVictoryEventListener implements SystemEventListener<SystemGoalReachedEvent> {

    final private GoalVictoryRepository victoryRepository;

    public SystemGoalReachedVictoryEventListener(GoalVictoryRepository victoryRepository) {
        this.victoryRepository = victoryRepository;
    }

    @Override
    public void onEvent(@Valid SystemGoalReachedEvent event) {
        // Step 1. Generating new victory
        GoalVictory newVictory = GoalVictory.create(event.getState());
        // Step 2. Saving new victory in repo
        victoryRepository.save(newVictory);
    }

    @Override
    public String getChannel() {
        return SystemGoalReachedEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemGoalReachedEvent.CHANNEL + " > goal:management";
    }
}
