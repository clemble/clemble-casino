package com.gogomaya.server.web.game.session;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.GameState;
import com.gogomaya.game.event.client.MadeMove;
import com.gogomaya.game.service.GameActionService;
import com.gogomaya.server.game.action.GameSessionProcessor;
import com.gogomaya.server.repository.game.GameSessionRepository;
import com.gogomaya.web.game.GameWebMapping;
import com.gogomaya.web.mapping.WebMapping;

@Controller
public class GameActionController<State extends GameState> implements GameActionService<State> {

    final private GameSessionProcessor<State> sessionProcessor;
    final private GameSessionRepository<State> sessionRepository;

    public GameActionController(final GameSessionRepository<State> sessionRepository, final GameSessionProcessor<State> sessionProcessor) {
        this.sessionProcessor = checkNotNull(sessionProcessor);
        this.sessionRepository = checkNotNull(sessionRepository);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = GameWebMapping.GAME_SESSION_ACTIONS, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody State process(@PathVariable("sessionId") long sessionId, @RequestBody ClientEvent move) {
        // Step 1. Retrieving associated table
        return sessionProcessor.process(sessionId, move);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GameWebMapping.GAME_SESSION_ACTIONS_ACTION, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody MadeMove getAction(@PathVariable("sessionId") long sessionId, @PathVariable("actionId") int actionId) {
        return sessionRepository.findAction(sessionId, actionId);
    }
}
