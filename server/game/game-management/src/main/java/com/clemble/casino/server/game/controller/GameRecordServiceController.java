package com.clemble.casino.server.game.controller;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.clemble.casino.game.lifecycle.record.GameRecord;
import com.clemble.casino.game.lifecycle.record.service.GameRecordService;
import com.clemble.casino.server.ExternalController;
import com.clemble.casino.server.game.repository.GameRecordRepository;
import com.clemble.casino.game.GameWebMapping;
import com.clemble.casino.WebMapping;

import java.util.List;

@RestController
public class GameRecordServiceController implements GameRecordService, ExternalController {

    final private GameRecordRepository recordRepository;

    public GameRecordServiceController(GameRecordRepository recordRepository) {
        this.recordRepository = checkNotNull(recordRepository);
    }

    @Override
    public List<GameRecord> myRecords() {
        throw new IllegalAccessError();
    }

    @RequestMapping(method = RequestMethod.GET, value = GameWebMapping.MY_RECORDS, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public List<GameRecord> myRecords(@CookieValue("player") String player) {
        return recordRepository.findByPlayers(player);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GameWebMapping.SESSIONS_RECORD, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public GameRecord get(@PathVariable("session") String sessionKey) {
        return recordRepository.findOne(sessionKey);
    }
}
