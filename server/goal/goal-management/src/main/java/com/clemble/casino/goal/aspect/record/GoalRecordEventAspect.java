package com.clemble.casino.goal.aspect.record;

import com.clemble.casino.client.event.EventSelector;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.lifecycle.management.event.GameEndedEvent;
import com.clemble.casino.goal.GoalAware;
import com.clemble.casino.goal.aspect.GoalAspect;
import com.clemble.casino.goal.lifecycle.management.event.GoalEndedEvent;
import com.clemble.casino.goal.lifecycle.record.GoalRecord;
import com.clemble.casino.goal.lifecycle.record.event.GoalRecordCreatedEvent;
import com.clemble.casino.goal.repository.GoalRecordRepository;
import com.clemble.casino.lifecycle.record.EventRecord;
import com.clemble.casino.lifecycle.record.RecordState;
import com.clemble.casino.server.player.notification.PlayerNotificationService;

import java.util.Date;

/**
 * Created by mavarazy on 10/03/14.
 */
public class GoalRecordEventAspect extends GoalAspect<Event> implements GoalAware {

    final private String goalKey;
    final private GoalRecordRepository recordRepository;
    final private PlayerNotificationService notificationService;

    public GoalRecordEventAspect(
        String goalKey,
        GoalRecordRepository recordRepository,
        PlayerNotificationService notificationService) {
        super(EventSelector.TRUE);
        this.goalKey = goalKey;
        this.notificationService = notificationService;
        this.recordRepository = recordRepository;
    }

    @Override
    public String getGoalKey() {
        return goalKey;
    }

    @Override
    protected void doEvent(Event event) {
        // Step 1. Constructing event record
        EventRecord move = new EventRecord(event, new Date());
        // Step 2. Saving event record
        GoalRecord record = recordRepository.findOne(goalKey);
        record.getEventRecords().add(move);
        // Step 3. Specifying ended state for the event
        if(event instanceof GoalEndedEvent) {
            // TODO Fishy bullshit (record is not immutable) be more consistent
            // TODO Create record after Payment complete, providing safe state mechanism for Record initiation
            record = record.copy(RecordState.finished, ((GoalEndedEvent) event).getOutcome());
            // Step 3.1. Send create notification
            notificationService.send(GoalRecordCreatedEvent.create(record));
        }
        // Step 4. Serializing record
        recordRepository.save(record);
    }

}
