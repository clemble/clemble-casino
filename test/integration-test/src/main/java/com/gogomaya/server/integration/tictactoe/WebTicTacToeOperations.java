package com.gogomaya.server.integration.tictactoe;

import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeMove;
import com.gogomaya.server.integration.game.GameOperations;
import com.gogomaya.server.integration.game.listener.GameListenerOperations;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.web.active.session.GameEngineController;

public class WebTicTacToeOperations extends AbstractTicTacToeOperations {
    
    final private GameEngineController<TicTacToeState> gameEngineController;

    public WebTicTacToeOperations(PlayerOperations playerOperations,
            GameOperations<TicTacToeState> gameOperations,
            GameListenerOperations<TicTacToeState> tableListenerOperations,
            GameEngineController<TicTacToeState> gameEngineController) {
        super(playerOperations, gameOperations, tableListenerOperations);
        this.gameEngineController = gameEngineController;
    }

    @Override
    public TicTacToeState perform(TicTacToePlayer player, TicTacToeMove action) {
        // Step 0. Parsing player, session and table identifiers
        long playerId = player.getPlayer().getPlayerId();
        long tableId = player.getTableId();
        long sessionId = player.getSessionId();
        // Step 1. Processing action by controller
        TicTacToeState updatedState = (TicTacToeState) gameEngineController.process(playerId,
                sessionId,
                tableId,
                action);
        // Step 2. Updating table for player
        player.setState(updatedState);
        // Step 3. Returning updated state
        return updatedState;
    }


}
