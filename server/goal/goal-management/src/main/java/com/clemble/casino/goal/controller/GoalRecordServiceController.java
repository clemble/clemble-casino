package com.clemble.casino.goal.controller;

import com.clemble.casino.goal.GoalWebMapping;
import com.clemble.casino.goal.lifecycle.record.GoalRecord;
import com.clemble.casino.goal.lifecycle.record.service.GoalRecordService;
import com.clemble.casino.goal.repository.GoalRecordRepository;
import com.clemble.casino.WebMapping;
import com.clemble.casino.lifecycle.record.RecordState;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by mavarazy on 9/20/14.
 */
@RestController
public class GoalRecordServiceController implements GoalRecordService {

    final private GoalRecordRepository recordRepository;

    public GoalRecordServiceController(GoalRecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @Override
    public List<GoalRecord> myRecords() {
        throw new IllegalAccessError();
    }


    @RequestMapping(method = RequestMethod.GET, value = GoalWebMapping.MY_RECORDS, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public List<GoalRecord> myRecords(@CookieValue("player") String player) {
        return recordRepository.findByPlayer(player);
    }

    @Override
    public List<GoalRecord> myRecordsWithState(RecordState state) {
        throw new IllegalAccessError();
    }

    @RequestMapping(method = RequestMethod.GET, value = GoalWebMapping.MY_RECORDS_STATE, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public List<GoalRecord> myRecordsWithState(@CookieValue("player") String player, @PathVariable("state") RecordState state) {
        return recordRepository.findByPlayerAndState(player, state);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GoalWebMapping.GOAL_RECORD, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public GoalRecord get(@PathVariable("goalKey") String key) {
        return recordRepository.findOne(key);
    }
}
