package com.clemble.casino.goal.aspect.record;

import com.clemble.casino.client.event.EventSelector;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.lifecycle.management.event.GameEndedEvent;
import com.clemble.casino.goal.GoalAware;
import com.clemble.casino.goal.aspect.GoalAspect;
import com.clemble.casino.goal.lifecycle.record.GoalRecord;
import com.clemble.casino.goal.repository.GoalRecordRepository;
import com.clemble.casino.lifecycle.record.EventRecord;
import com.clemble.casino.lifecycle.record.RecordState;

import java.util.Date;

/**
 * Created by mavarazy on 10/03/14.
 */
public class GoalRecordEventAspect extends GoalAspect<Event> implements GoalAware {

    final private String goalKey;
    final private GoalRecordRepository recordRepository;

    public GoalRecordEventAspect(String goalKey, GoalRecordRepository recordRepository) {
        super(EventSelector.TRUE);
        this.goalKey = goalKey;
        this.recordRepository = recordRepository;
    }

    @Override
    public String getGoalKey() {
        return goalKey;
    }

    @Override
    public void doEvent(Event event) {
        // Step 1. Constructing event record
        EventRecord move = new EventRecord(event, new Date());
        // Step 2. Saving event record
        GoalRecord record = recordRepository.findOne(goalKey);
        record.getEventRecords().add(move);
        // Step 3. Specifying ended state for the event
        if(event instanceof GameEndedEvent) {
            // Fishy bullshit be more consistent
            record = record.copy(RecordState.finished);
        }
        // Step 4. Serializing record
        recordRepository.save(record);
    }

}
