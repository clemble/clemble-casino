package com.clemble.casino.server.web.game.session;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.event.client.MadeMove;
import com.clemble.casino.game.service.GameActionService;
import com.clemble.casino.server.game.action.GameSessionProcessor;
import com.clemble.casino.server.repository.game.GameSessionRepository;
import com.clemble.casino.web.mapping.WebMapping;
import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.web.game.GameWebMapping;

@Controller
public class GameActionController<State extends GameState> implements GameActionService<State> {

    final private Game game;
    final private GameSessionProcessor<State> sessionProcessor;
    final private GameSessionRepository<State> sessionRepository;

    public GameActionController(final Game game, final GameSessionRepository<State> sessionRepository, final GameSessionProcessor<State> sessionProcessor) {
        this.game = checkNotNull(game);
        this.sessionProcessor = checkNotNull(sessionProcessor);
        this.sessionRepository = checkNotNull(sessionRepository);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = GameWebMapping.GAME_SESSIONS_ACTIONS, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody State process(@PathVariable("sessionId") long session, @RequestBody ClientEvent move) {
        // Step 1. Retrieving associated table
        return sessionProcessor.process(new GameSessionKey(game, session), move);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GameWebMapping.GAME_SESSIONS_ACTIONS_ACTION, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody MadeMove getAction(@PathVariable("sessionId") long session, @PathVariable("actionId") int actionId) {
        return sessionRepository.findAction(new GameSessionKey(game, session), actionId);
    }
}