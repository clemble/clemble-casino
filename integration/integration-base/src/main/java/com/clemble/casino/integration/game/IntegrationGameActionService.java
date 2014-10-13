package com.clemble.casino.integration.game;

import com.clemble.casino.game.lifecycle.management.GameContext;
import com.clemble.casino.game.lifecycle.management.GameState;
import com.clemble.casino.game.lifecycle.management.event.GameManagementEvent;
import com.clemble.casino.game.lifecycle.management.service.GameActionService;
import com.clemble.casino.lifecycle.management.event.action.Action;
import com.clemble.casino.server.game.controller.GameActionController;

/**
 * Created by mavarazy on 13/10/14.
 */
public class IntegrationGameActionService implements GameActionService{

    final private String player;
    final private GameActionController controller;

    public IntegrationGameActionService(String player, GameActionController controller) {
        this.player = player;
        this.controller = controller;
    }

    @Override
    public GameState getState(String sessionKey) {
        return controller.getState(sessionKey);
    }

    @Override
    public GameManagementEvent process(String sessionKey, Action action) {
        return controller.process(sessionKey, player, action);
    }

    @Override
    public GameContext<?> getContext(String sessionKey) {
        return controller.getContext(sessionKey);
    }
}
