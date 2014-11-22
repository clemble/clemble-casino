package com.clemble.casino.goal.action;

import com.clemble.casino.goal.event.GoalEvent;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.initiation.GoalInitiation;
import com.clemble.casino.goal.lifecycle.management.GoalContext;
import com.clemble.casino.goal.lifecycle.management.GoalPlayerContext;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.record.GoalRecord;
import com.clemble.casino.goal.repository.GoalRecordRepository;
import com.clemble.casino.goal.repository.GoalStateRepository;
import com.clemble.casino.lifecycle.configuration.rule.time.PlayerClock;
import com.clemble.casino.server.action.ClembleManager;
import com.clemble.casino.server.action.ClembleManagerFactory;
import com.clemble.casino.server.player.notification.PlayerNotificationService;

import java.util.Collections;

/**
 * Created by mavarazy on 10/9/14.
 */
public class SelfGoalManagerFactory implements GoalManagerFactory {

    final private GoalRecordRepository recordRepository;
    final private GoalStateRepository stateRepository;
    final private PlayerNotificationService notificationService;
    final private ClembleManagerFactory<GoalConfiguration> managerFactory;

    public SelfGoalManagerFactory(
        ClembleManagerFactory<GoalConfiguration> managerFactory,
        GoalRecordRepository recordRepository,
        GoalStateRepository stateRepository,
        PlayerNotificationService notificationService) {
        this.managerFactory = managerFactory;
        this.recordRepository = recordRepository;
        this.stateRepository = stateRepository;
        this.notificationService = notificationService;
    }

    @Override
    public ClembleManager<GoalEvent, ? extends GoalState> start(GoalContext parent, GoalInitiation initiation) {
        // Step 1. Saving record
        GoalRecord record = recordRepository.save(initiation.toRecord());
        // Step 2. Creating state
        GoalPlayerContext playerContext = new GoalPlayerContext(initiation.getPlayer(), new PlayerClock(0, 0, 0, null));
        GoalContext goalContext = new GoalContext(parent, Collections.singletonList(playerContext));
        GoalState state = new GoalState(initiation.getGoalKey(), initiation.getPlayer(), record.getBids(), initiation.getGoal(), initiation.getConfiguration(), goalContext, "Go for it");
        // Step 3. Saving state
        stateRepository.save(state);
        // Step 4. Creating manager factory
        return managerFactory.create(state, state.getConfiguration());
    }

    public ClembleManager<GoalEvent, GoalState> get(String goalKey) {
        // Step 1. Fetching current goal state
        GoalState state = stateRepository.findOne(goalKey);
        if (state == null)
            return null;
        // Step 2. Creating appropriate goal manager
        return managerFactory.create(state, state.getConfiguration());
    }

}
