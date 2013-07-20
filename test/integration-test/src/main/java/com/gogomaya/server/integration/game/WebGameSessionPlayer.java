package com.gogomaya.server.integration.game;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.ServerResourse;
import com.gogomaya.server.game.construct.GameConstruction;
import com.gogomaya.server.game.event.client.GameClientEvent;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.web.active.session.GameActionController;

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
        // Step 0. Parsing player, session and table identifiers
        long playerId = clientEvent.getPlayerId();
        long tableId = resourse.getTableId();
        // Step 1. Processing action by controller
        return (State) gameEngineController.process(playerId, session, session, tableId, clientEvent);
    }

}
