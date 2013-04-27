package com.gogomaya.server.integration.tictactoe;

import com.gogomaya.server.game.tictactoe.action.TicTacToeTable;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeMove;
import com.gogomaya.server.integration.game.GameOperations;
import com.gogomaya.server.integration.game.listener.GameListenerOperations;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.web.active.session.GameEngineController;

public class WebTicTacToeOperations extends AbstractTicTacToeOperations {
    
    final private GameEngineController gameEngineController;

    public WebTicTacToeOperations(PlayerOperations playerOperations,
            GameOperations gameOperations,
            GameListenerOperations<TicTacToeTable> tableListenerOperations,
            GameEngineController gameEngineController) {
        super(playerOperations, gameOperations, tableListenerOperations);
        this.gameEngineController = gameEngineController;
    }

    @Override
    public void perform(TicTacToePlayer player, TicTacToeMove action) {
        // Step 1. Processing action by controller
        TicTacToeTable updatedTable = gameEngineController.process(player.getPlayer().getPlayerId(),
                player.getTable().getCurrentSession().getSessionId(),
                player.getTable().getTableId(),
                action);
        // Step 2. Updating table for player
        player.setTable(updatedTable);
    }


}
