package com.clemble.casino.server.game.controller;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.clemble.casino.game.GameRecord;
import com.clemble.casino.game.service.GameRecordService;
import com.clemble.casino.server.ExternalController;
import com.clemble.casino.server.game.repository.GameRecordRepository;
import com.clemble.casino.game.GameWebMapping;
import com.clemble.casino.web.mapping.WebMapping;

@RestController
public class GameRecordServiceController implements GameRecordService, ExternalController {

    final private GameRecordRepository recordRepository;

    public GameRecordServiceController(GameRecordRepository recordRepository) {
        this.recordRepository = checkNotNull(recordRepository);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GameWebMapping.SESSIONS_RECORD, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public GameRecord get(@PathVariable("session") String sessionKey) {
        return recordRepository.findOne(sessionKey);
    }
}
