package com.gogomaya.server.web.game.session;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.GameState;
import com.gogomaya.game.event.client.MadeMove;
import com.gogomaya.server.game.action.GameSessionProcessor;
import com.gogomaya.server.repository.game.GameSessionRepository;
import com.gogomaya.web.mapping.GameWebMapping;

@Controller
public class GameActionController<State extends GameState> {

    final private GameSessionProcessor<State> sessionProcessor;
    final private GameSessionRepository<State> sessionRepository;

    public GameActionController(final GameSessionRepository<State> sessionRepository, final GameSessionProcessor<State> sessionProcessor) {
        this.sessionProcessor = checkNotNull(sessionProcessor);
        this.sessionRepository = checkNotNull(sessionRepository);
    }

    @RequestMapping(method = RequestMethod.POST, value = GameWebMapping.GAME_SESSION_ACTIONS, produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    State process(
            @RequestHeader("playerId") long playerId,
            @RequestHeader("sessionId") long sessionId,
            @PathVariable("sessionId") long requestSessionId,
            @RequestHeader("tableId") long tableId,
            @RequestBody ClientEvent move) {
        // Step 1. Retrieving associated table
        return sessionProcessor.process(sessionId, move);
    }

    @RequestMapping(method = RequestMethod.GET, value = GameWebMapping.GAME_SESSION_ACTIONS_ACTION, produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    MadeMove getAction(@RequestHeader("playerId") long playerId, @PathVariable("sessionId") long sessionId, @PathVariable("actionId") int actionId) {
        return sessionRepository.findAction(sessionId, actionId);
    }
}
