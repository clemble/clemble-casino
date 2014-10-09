package com.clemble.casino.goal.action;

import com.clemble.casino.goal.event.GoalEvent;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.initiation.GoalInitiation;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.repository.GoalRecordRepository;
import com.clemble.casino.server.action.ClembleManager;
import com.clemble.casino.server.action.ClembleManagerFactory;

/**
 * Created by mavarazy on 10/9/14.
 */
public class SelfGoalManagerFactory implements GoalManagerFactory {

    final private GoalRecordRepository recordRepository;
    final private ClembleManagerFactory<GoalConfiguration> managerFactory;

    public SelfGoalManagerFactory(
        ClembleManagerFactory<GoalConfiguration> managerFactory,
        GoalRecordRepository recordRepository) {
        this.managerFactory = managerFactory;
        this.recordRepository = recordRepository;
    }


    @Override
    public ClembleManager<GoalEvent, ? extends GoalState> start(GoalInitiation initiation) {
        return null;
    }
}
