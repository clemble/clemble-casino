package com.clemble.casino.server.repository.game;

import com.clemble.casino.game.GameRecord;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.action.MadeMove;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MadeMoveRepository {

    final private Logger LOG = LoggerFactory.getLogger(MadeMoveRepository.class);

    final private GameRecordRepository recordRepository;

    public MadeMoveRepository(GameRecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public void save(GameSessionKey sessionKey, MadeMove move) {
        GameRecord record = recordRepository.findOne(sessionKey);
        record.getMadeMoves().add(move);
        recordRepository.save(record);
    }
}
