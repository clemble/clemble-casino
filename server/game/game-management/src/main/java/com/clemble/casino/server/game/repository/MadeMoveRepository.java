package com.clemble.casino.server.game.repository;

import com.clemble.casino.game.GameRecord;
import com.clemble.casino.lifecycle.management.EventRecord;

public class MadeMoveRepository {

    final private GameRecordRepository recordRepository;

    public MadeMoveRepository(GameRecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public void save(String sessionKey, EventRecord move) {
        GameRecord record = recordRepository.findOne(sessionKey);
        record.getEventRecords().add(move);
        recordRepository.save(record);
    }
}
