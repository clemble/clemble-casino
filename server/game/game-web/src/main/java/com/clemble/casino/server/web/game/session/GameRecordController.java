package com.clemble.casino.server.web.game.session;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameRecord;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.service.GameRecordService;
import com.clemble.casino.server.ExternalController;
import com.clemble.casino.server.repository.game.MatchGameRecordRepository;
import com.clemble.casino.server.repository.game.PotGameRecordRepository;
import com.clemble.casino.web.game.GameWebMapping;
import com.clemble.casino.web.mapping.WebMapping;

@Controller
public class GameRecordController implements GameRecordService, ExternalController {

    final private MatchGameRecordRepository matchGameRecordRepository;
    final private PotGameRecordRepository potGameRecordRepository;

    public GameRecordController(MatchGameRecordRepository matchGameRecordRepository, PotGameRecordRepository potGameRecordRepository) {
        this.matchGameRecordRepository = checkNotNull(matchGameRecordRepository);
        this.potGameRecordRepository = checkNotNull(potGameRecordRepository);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GameWebMapping.GAME_SESSIONS_RECORD, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody GameRecord get(@PathVariable("game") Game game, @PathVariable("session") String session) {
        GameSessionKey sessionKey = new GameSessionKey(game, session);
        if (matchGameRecordRepository.exists(sessionKey))
            return matchGameRecordRepository.findOne(sessionKey);
        else if (potGameRecordRepository.exists(sessionKey))
            return potGameRecordRepository.findOne(sessionKey);
        return null;
    }
}
