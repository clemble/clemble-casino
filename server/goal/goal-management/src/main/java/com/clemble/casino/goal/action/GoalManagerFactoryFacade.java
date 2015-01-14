package com.clemble.casino.goal.action;

import com.clemble.casino.goal.event.GoalEvent;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.initiation.GoalInitiation;
import com.clemble.casino.goal.lifecycle.management.GoalContext;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.repository.GoalRecordRepository;
import com.clemble.casino.goal.repository.GoalStateRepository;
import com.clemble.casino.server.action.ClembleManager;
import com.clemble.casino.server.action.ClembleManagerFactory;
import com.clemble.casino.server.player.notification.ServerNotificationService;

/**
 * Created by mavarazy on 9/20/14.
 */
public class GoalManagerFactoryFacade {

    final private GoalStateRepository stateRepository;
    final private ShortGoalManagerFactory shortGoalManagerFactory;

    public GoalManagerFactoryFacade(
        ClembleManagerFactory<GoalConfiguration> shortGoalManagerFactory,
        GoalRecordRepository recordRepository,
        GoalStateRepository stateRepository,
        ServerNotificationService notificationService) {
        this.stateRepository = stateRepository;
        this.shortGoalManagerFactory = new ShortGoalManagerFactory(shortGoalManagerFactory, recordRepository, stateRepository, notificationService);
    }

    public ClembleManager<GoalEvent, ? extends GoalState> start(GoalContext parent, GoalInitiation initiation) {
        // Step 1. Creating manager
        ClembleManager<GoalEvent, ? extends GoalState> manager = shortGoalManagerFactory.start(initiation, parent);
        // Step 2. Starting manager
        manager.start();
        // Step 3. Returning created manager
        return manager;
    }

    public ClembleManager<GoalEvent, ? extends GoalState> get(String goalKey) {
        GoalState state = stateRepository.findOne(goalKey);
        if (state instanceof GoalState) {
            return shortGoalManagerFactory.create((GoalState) state);
        }
        return null;
    }

}
