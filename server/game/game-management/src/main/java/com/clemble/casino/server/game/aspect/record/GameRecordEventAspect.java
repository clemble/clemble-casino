package com.clemble.casino.server.game.aspect.record;

import com.clemble.casino.client.event.EventSelector;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameSessionAware;
import com.clemble.casino.game.lifecycle.record.GameRecord;
import com.clemble.casino.game.lifecycle.management.event.GameEndedEvent;
import com.clemble.casino.lifecycle.record.EventRecord;
import com.clemble.casino.lifecycle.record.RecordState;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.repository.GameRecordRepository;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Date;

/**
 * Created by mavarazy on 10/03/14.
 */
public class GameRecordEventAspect extends GameAspect<Event> implements GameSessionAware {

    final private String sessionKey;
    final private GameRecordRepository recordRepository;

    public GameRecordEventAspect(String sessionKey, GameRecordRepository recordRepository) {
        super(EventSelector.TRUE);
        this.sessionKey = sessionKey;
        this.recordRepository = recordRepository;
    }

    @Override
    public String getSessionKey() {
        return sessionKey;
    }

    @Override
    protected void doEvent(Event event) {
        // Step 1. Constructing event record
        EventRecord move = new EventRecord(event, DateTime.now(DateTimeZone.UTC));
        // Step 2. Saving event record
        GameRecord record = recordRepository.findOne(sessionKey);
        record.getEventRecords().add(move);
        // Step 3. Specifying ended state for the event
        if(event instanceof GameEndedEvent)
            record = record.copy(RecordState.finished, ((GameEndedEvent) event).getOutcome());
        // Step 4. Serializing record
        recordRepository.save(record);
    }

}
