package com.gogomaya.server.integration.tictactoe;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.tictactoe.TicTacToeState;
import com.gogomaya.server.web.active.session.GameEngineController;

public class WebTicTacToeOperations extends AbstractTicTacToeOperations {

    final private GameEngineController<TicTacToeState> gameEngineController;

    public WebTicTacToeOperations(final GameEngineController<TicTacToeState> gameEngineController) {
        this.gameEngineController = gameEngineController;
    }

    @Override
    public TicTacToeState perform(TicTacToePlayer player, ClientEvent action) {
        // Step 0. Parsing player, session and table identifiers
        long playerId = player.getPlayer().getPlayerId();
        long tableId = player.getTableId();
        long session = player.getSession();
        // Step 1. Processing action by controller
        TicTacToeState updatedState = (TicTacToeState) gameEngineController.process(playerId, session, tableId, action);
        // Step 2. Updating table for player
        player.setState(updatedState);
        // Step 3. Returning updated state
        return updatedState;
    }

}
