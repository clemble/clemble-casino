package com.clemble.casino.integration.game;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.ServerResourse;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.event.client.GameClientEvent;
import com.clemble.casino.integration.player.Player;
import com.clemble.casino.server.web.game.session.GameActionController;

public class WebGameSessionPlayer<State extends GameState> extends AbstractGameSessionPlayer<State> {

    /**
     * Generated 05/07/13
     */
    private static final long serialVersionUID = 694894192572157764L;

    final private GameActionController<State> gameEngineController;

    public WebGameSessionPlayer(final Player player, final GameConstruction construction, final GameActionController<State> gameEngineController) {
        super(player, construction);
        this.gameEngineController = checkNotNull(gameEngineController);
    }

    @Override
    public State perform(Player player, ServerResourse resourse, GameSessionKey session, GameClientEvent clientEvent) {
        // Step 1. Processing action by controller
        return (State) gameEngineController.process(session.getSession(), clientEvent);
    }

}
