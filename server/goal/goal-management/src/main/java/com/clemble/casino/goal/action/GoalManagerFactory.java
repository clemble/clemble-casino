package com.clemble.casino.goal.action;

import com.clemble.casino.goal.lifecycle.initiation.GoalInitiation;
import com.clemble.casino.goal.repository.GoalRecordRepository;

/**
 * Created by mavarazy on 9/20/14.
 */
public class GoalManagerFactory {

    final private GoalRecordRepository recordRepository;

    public GoalManagerFactory(GoalRecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public void start(GoalInitiation initiation) {
        recordRepository.save(initiation.toRecord());
    }
}
