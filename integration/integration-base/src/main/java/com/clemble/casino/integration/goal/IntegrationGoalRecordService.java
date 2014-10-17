package com.clemble.casino.integration.goal;

import com.clemble.casino.goal.controller.GoalRecordServiceController;
import com.clemble.casino.goal.lifecycle.record.GoalRecord;
import com.clemble.casino.goal.lifecycle.record.service.GoalRecordService;
import com.clemble.casino.lifecycle.record.RecordState;

import java.util.List;

/**
 * Created by mavarazy on 17/10/14.
 */
public class IntegrationGoalRecordService implements GoalRecordService {

    final private String player;
    final private GoalRecordServiceController recordService;

    public IntegrationGoalRecordService(String player, GoalRecordServiceController recordService) {
        this.player = player;
        this.recordService = recordService;
    }

    @Override
    public List<GoalRecord> myRecords() {
        return recordService.myRecords(player);
    }

    @Override
    public List<GoalRecord> myRecordsWithState(RecordState state) {
        return recordService.myRecordsWithState(player, state);
    }

    @Override
    public GoalRecord get(String key) {
        return recordService.get(key);
    }

}
