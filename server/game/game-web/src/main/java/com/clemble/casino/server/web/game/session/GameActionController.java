package com.clemble.casino.server.web.game.session;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.game.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.clemble.casino.game.action.GameAction;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.game.service.GameActionService;
import com.clemble.casino.server.ExternalController;
import com.clemble.casino.server.game.action.GameManager;
import com.clemble.casino.server.game.action.GameManagerFactory;
import com.clemble.casino.server.repository.game.GameRecordRepository;
import com.clemble.casino.web.game.GameWebMapping;
import com.clemble.casino.web.mapping.WebMapping;

@Controller
public class GameActionController<State extends GameState> implements GameActionService, ExternalController {

    final private GameManagerFactory managerFactory;
    final private GameRecordRepository sessionRepository;

    public GameActionController(
            final GameRecordRepository sessionRepository,
            final GameManagerFactory sessionProcessor) {
        this.managerFactory = checkNotNull(sessionProcessor);
        this.sessionRepository = checkNotNull(sessionRepository);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = GameWebMapping.SESSIONS_ACTIONS, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody GameManagementEvent process(@PathVariable("game") Game game, @PathVariable("session") String session, @RequestBody GameAction action) {
        GameSessionKey sessionKey = new GameSessionKey(game, session);
        // Step 1. Retrieving associated table
        return managerFactory.get(sessionKey).process(action);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GameWebMapping.SESSIONS_STATE, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody GameState getState(@PathVariable("game") Game game, @PathVariable("session") String session) {
        // Step 1. Constructing session key
        GameSessionKey sessionKey = new GameSessionKey(game, session);
        // Step 2. Fetching game manager
        GameManager<?> gameManager = managerFactory.get(sessionKey);
        // Step 3. Fetching State
        return gameManager == null ? null : gameManager.getState();
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GameWebMapping.SESSIONS_CONTEXT, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody GameContext<?> getContext(@PathVariable("game") Game game, @PathVariable("session") String session) {
        // Step 1. Generating game session key
        GameSessionKey  sessionKey = new GameSessionKey(game, session);
        // Step 2. Extracting game context
        GameManager manager = managerFactory.get(sessionKey);
        return manager != null ? manager.getContext() : null;
    }

}
