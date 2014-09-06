package com.clemble.casino.server.game.aspect.record;

import com.clemble.casino.client.event.EventSelector;
import com.clemble.casino.event.Event;
import com.clemble.casino.event.GameEvent;
import com.clemble.casino.game.GameRecord;
import com.clemble.casino.game.action.GameEventRecord;
import com.clemble.casino.server.game.aspect.BasicGameAspect;
import com.clemble.casino.server.game.repository.GameRecordRepository;

import java.util.Date;

/**
 * Created by mavarazy on 10/03/14.
 */
public class GameRecordEventAspect extends BasicGameAspect<Event> {

    final private String sessionKey;
    final private GameRecordRepository recordRepository;

    public GameRecordEventAspect(String sessionKey, GameRecordRepository recordRepository) {
        super(EventSelector.TRUE);
        this.sessionKey = sessionKey;
        this.recordRepository = recordRepository;
    }

    @Override
    public void doEvent(Event event) {
        // Step 1. Constructing event record
        GameEventRecord move = new GameEventRecord()
            .setEvent(event)
                .setCreated(new Date());
        // Step 2. Saving event record
        GameRecord record = recordRepository.findOne(sessionKey);
        record.getEventRecords().add(move);
        recordRepository.save(record);
    }

}
