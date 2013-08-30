package com.gogomaya.server.integration.game;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.game.GameState;
import com.gogomaya.game.ServerResourse;
import com.gogomaya.game.construct.GameConstruction;
import com.gogomaya.game.event.client.GameClientEvent;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.web.game.session.GameActionController;

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
    public State perform(Player player, ServerResourse resourse, long session, GameClientEvent clientEvent) {
        // Step 1. Processing action by controller
        return (State) gameEngineController.process(session, clientEvent);
    }

}
