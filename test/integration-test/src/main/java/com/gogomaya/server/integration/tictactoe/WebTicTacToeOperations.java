package com.gogomaya.server.integration.tictactoe;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.event.schedule.InvitationResponceEvent;
import com.gogomaya.server.game.tictactoe.TicTacToeState;
import com.gogomaya.server.web.active.session.GameConstructionController;
import com.gogomaya.server.web.active.session.GameEngineController;

public class WebTicTacToeOperations extends AbstractTicTacToeOperations {

    final private GameEngineController<TicTacToeState> gameEngineController;
    final private GameConstructionController<TicTacToeState> constructionController;

    public WebTicTacToeOperations(final GameEngineController<TicTacToeState> gameEngineController,
            final GameConstructionController<TicTacToeState> constructionController) {
        this.gameEngineController = checkNotNull(gameEngineController);
        this.constructionController = checkNotNull(constructionController);
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

    @Override
    protected void responce(InvitationResponceEvent responceEvent) {
        constructionController.invitationResponsed(responceEvent.getPlayerId(), responceEvent);
    }

}
