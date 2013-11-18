package com.clemble.casino.integration.game;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.ServerRegistry;
import com.clemble.casino.client.game.GameActionOperations;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.event.client.GameClientEvent;
import com.clemble.casino.integration.player.Player;

public class SimpleGameSessionPlayer<State extends GameState> extends AbstractGameSessionPlayer<State> {

    /**
     * Generated 05/07/13
     */
    private static final long serialVersionUID = 694894192572157764L;

    final private GameActionOperations<State> gameEngineController;

    public SimpleGameSessionPlayer(final Player player, final GameConstruction construction, final GameActionOperations<State> gameEngineController) {
        super(player, construction);
        this.gameEngineController = checkNotNull(gameEngineController);
    }

    @Override
    public State perform(Player player, ServerRegistry resourse, GameSessionKey session, GameClientEvent clientEvent) {
        // Step 1. Processing action by controller
        return (State) gameEngineController.process(clientEvent);
    }

}
