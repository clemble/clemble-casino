package com.gogomaya.server.web.game.session;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gogomaya.game.Game;
import com.gogomaya.game.GameSession;
import com.gogomaya.game.GameSessionKey;
import com.gogomaya.game.GameState;
import com.gogomaya.server.repository.game.GameSessionRepository;
import com.gogomaya.web.game.GameWebMapping;
import com.gogomaya.web.mapping.WebMapping;

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
    public @ResponseBody GameSession<State> get(@RequestHeader("playerId") long playerId, @PathVariable("sessionId") long session) {
        return sessionRepository.findOne(new GameSessionKey(game, session));
    }
}
