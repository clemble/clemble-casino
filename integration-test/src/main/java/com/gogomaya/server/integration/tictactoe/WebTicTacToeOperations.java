package com.gogomaya.server.integration.tictactoe;

import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.game.tictactoe.action.TicTacToeTable;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeMove;
import com.gogomaya.server.integration.game.GameOperations;
import com.gogomaya.server.integration.game.listener.GameListenerOperations;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.web.active.session.GameEngineController;

public class WebTicTacToeOperations extends AbstractTicTacToeOperations {
    
    final private GameEngineController<TicTacToeState> gameEngineController;

    public WebTicTacToeOperations(PlayerOperations playerOperations,
            GameOperations gameOperations,
            GameListenerOperations<TicTacToeTable> tableListenerOperations,
            GameEngineController<TicTacToeState> gameEngineController) {
        super(playerOperations, gameOperations, tableListenerOperations);
        this.gameEngineController = gameEngineController;
    }

    @Override
    public void perform(TicTacToePlayer player, TicTacToeMove action) {
        // Step 0. Parsing player, session and table identifiers
        long playerId = player.getPlayer().getPlayerId();
        long tableId = player.getTable().getTableId();
        long sessionId = player.getTable().getCurrentSession().getSessionId();
        // Step 1. Processing action by controller
        TicTacToeTable updatedTable = (TicTacToeTable) gameEngineController.process(playerId,
                sessionId,
                tableId,
                action);
        // Step 2. Updating table for player
        player.setTable(updatedTable);
    }


}
