package com.clemble.casino.server.game.controller;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameRecord;
import com.clemble.casino.game.service.GameRecordService;
import com.clemble.casino.server.ExternalController;
import com.clemble.casino.server.game.repository.GameRecordRepository;
import com.clemble.casino.game.GameWebMapping;
import com.clemble.casino.web.mapping.WebMapping;

@RestController
public class GameRecordController implements GameRecordService, ExternalController {

    final private GameRecordRepository roundGameRecordRepository;

    public GameRecordController(GameRecordRepository roundGameRecordRepository) {
        this.roundGameRecordRepository = checkNotNull(roundGameRecordRepository);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GameWebMapping.SESSIONS_RECORD, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public GameRecord get(@PathVariable("session") String sessionKey) {
        return roundGameRecordRepository.findOne(sessionKey);
    }
}
