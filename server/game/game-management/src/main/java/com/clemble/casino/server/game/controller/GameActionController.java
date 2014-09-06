package com.clemble.casino.server.game.controller;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.GameState;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.game.service.GameActionService;
import com.clemble.casino.server.ExternalController;
import com.clemble.casino.server.game.action.GameManager;
import com.clemble.casino.server.game.action.GameManagerFactory;
import com.clemble.casino.server.game.repository.GameRecordRepository;
import com.clemble.casino.game.GameWebMapping;
import com.clemble.casino.web.mapping.WebMapping;

@RestController
public class GameActionController<State extends GameState> implements GameActionService, ExternalController {

    final private GameManagerFactory managerFactory;
    final private GameRecordRepository recordRepository;

    public GameActionController(
            final GameRecordRepository recordRepository,
            final GameManagerFactory sessionProcessor) {
        this.managerFactory = checkNotNull(sessionProcessor);
        this.recordRepository = checkNotNull(recordRepository);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = GameWebMapping.SESSIONS_ACTIONS, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public GameManagementEvent process(@PathVariable("session") String sessionKey, @RequestBody Event action) {
        // Step 1. Retrieving associated table
        return managerFactory.get(sessionKey).process(action);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GameWebMapping.SESSIONS_STATE, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public GameState getState(@PathVariable("session") String sessionKey) {
        // Step 1. Fetching game manager
        GameManager<?> gameManager = managerFactory.get(sessionKey);
        // Step 2. Fetching State
        return gameManager == null ? null : gameManager.getState();
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GameWebMapping.SESSIONS_CONTEXT, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public GameContext<?> getContext(@PathVariable("session") String sessionKey) {
        // Step 1. Extracting game context
        GameManager manager = managerFactory.get(sessionKey);
        return manager != null ? manager.getContext() : null;
    }

}
