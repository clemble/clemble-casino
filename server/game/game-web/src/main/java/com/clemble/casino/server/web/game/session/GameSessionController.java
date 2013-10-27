package com.clemble.casino.server.web.game.session;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.server.repository.game.GameSessionRepository;
import com.clemble.casino.web.game.GameWebMapping;
import com.clemble.casino.web.mapping.WebMapping;

@Controller
public class GameSessionController<State extends GameState> {

    final private Game game;
    final private GameSessionRepository<State> sessionRepository;

    public GameSessionController(Game game, GameSessionRepository<State> sessionRepository) {
        this.game = checkNotNull(game);
        this.sessionRepository = checkNotNull(sessionRepository);
    }

    @RequestMapping(method = RequestMethod.GET, value = GameWebMapping.GAME_SESSIONS_SESSION, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody GameSession<State> get(@RequestHeader("playerId") String playerId, @PathVariable("sessionId") String session) {
        return sessionRepository.findOne(new GameSessionKey(game, session));
    }
}
