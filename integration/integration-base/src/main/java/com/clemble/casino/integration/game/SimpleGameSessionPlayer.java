package com.clemble.casino.integration.game;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.ServerRegistry;
import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.game.GameActionOperations;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.action.GameAction;
import com.clemble.casino.game.construct.GameConstruction;

public class SimpleGameSessionPlayer<State extends GameState> extends AbstractGameSessionPlayer<State> {

    /**
     * Generated 05/07/13
     */
    private static final long serialVersionUID = 694894192572157764L;

    final private GameActionOperations<State> gameEngineController;

    public SimpleGameSessionPlayer(final ClembleCasinoOperations player, final GameConstruction construction, final GameActionOperations<State> gameEngineController) {
        super(player, construction);
        this.gameEngineController = checkNotNull(gameEngineController);
    }

    @Override
    public State perform(ClembleCasinoOperations player, ServerRegistry resourse, GameSessionKey session, GameAction clientEvent) {
        // Step 1. Processing action by controller
        return (State) gameEngineController.process(clientEvent);
    }

}
