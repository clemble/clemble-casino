package com.clemble.casino.server.repository.game;

import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.RoundGameRecord;
import com.clemble.casino.game.action.MadeMove;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class MadeMoveRepository {

    final private Logger LOG = LoggerFactory.getLogger(MadeMoveRepository.class);

    final private RoundGameRecordRepository recordRepository;

    public MadeMoveRepository(RoundGameRecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public void save(GameSessionKey sessionKey, MadeMove move) {
        RoundGameRecord record = recordRepository.findOne(sessionKey);
        record.getMadeMoves().add(move);
        recordRepository.save(record);
    }
}
