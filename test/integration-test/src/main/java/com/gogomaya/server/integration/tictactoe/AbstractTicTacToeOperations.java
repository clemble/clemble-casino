package com.gogomaya.server.integration.tictactoe;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.gogomaya.server.event.GogomayaEvent;
import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.event.GameEvent;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.tictactoe.action.TicTacToeCell;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeBetOnCellMove;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeMove;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeSelectCellMove;
import com.gogomaya.server.integration.game.GameOperations;
import com.gogomaya.server.integration.game.listener.GameListener;
import com.gogomaya.server.integration.game.listener.GameListenerOperations;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.google.common.collect.ImmutableList;

abstract public class AbstractTicTacToeOperations implements TicTacToeOperations {

    final private PlayerOperations playerOperations;
    final private GameOperations<TicTacToeState> gameOperations;
    final private GameListenerOperations<TicTacToeState> tableListenerOperations;

    public AbstractTicTacToeOperations(PlayerOperations playerOperations,
            final GameOperations<TicTacToeState> gameOperations,
            final GameListenerOperations<TicTacToeState> tableListenerOperations) {
        this.playerOperations = checkNotNull(playerOperations);
        this.gameOperations = checkNotNull(gameOperations);
        this.tableListenerOperations = checkNotNull(tableListenerOperations);
    }

    final public List<TicTacToePlayer> start() {
        // Step 1. Selecting specification for the game
        GameSpecification specification = gameOperations.selectSpecification();
        // Step 2. Creating user and trying to put them on the same table
        TicTacToePlayer playerA = start(specification);
        TicTacToePlayer playerB = start(specification);
        while (playerA.getTableId() != playerB.getTableId()) {
            playerA.getListenerControl().stopListener();
            playerA = start(specification);
            // waits added to be sure everyone on the same page
            if (playerA.getTableId() != playerB.getTableId()) {
                playerB.getListenerControl().stopListener();
                playerB = start(specification);
            }
        }
        TicTacToePlayerUtils.syncVersions(playerA, playerB);
        // Step 3. Returning generated value who ever goes first is choosen as first
        TicTacToeState state = playerB.getState() != null ? playerB.getState() : playerA.getState();
        if (state.getNextMove(playerA.getPlayer().getPlayerId()) == null) {
            return ImmutableList.<TicTacToePlayer> of(playerB, playerA);
        } else {
            return ImmutableList.<TicTacToePlayer> of(playerA, playerB);
        }
    }

    final public TicTacToePlayer start(GameSpecification specification) {
        // Step 1. Creating player
        Player player = checkNotNull(playerOperations.createPlayer());
        // Step 2. Requesting table
        final GameTable<TicTacToeState> table = (GameTable<TicTacToeState>) checkNotNull(gameOperations.start(player, specification));
        final TicTacToePlayer toePlayer = new TicTacToePlayer(player, table, this);
        // Step 3. Creating listener, that will update GameListener
        toePlayer.setListenerControl(tableListenerOperations.listen(table, new GameListener() {

            @Override
            public void updated(GogomayaEvent event) {
                if(event instanceof GameEvent)
                    toePlayer.setState(((GameEvent<TicTacToeState>) event).getState());
            }

        }));
        // Step 3. Creating player configurations
        return toePlayer;
    }

    final public TicTacToeState select(TicTacToePlayer player, int row, int column) {
        // Step 1. Generating bet move
        TicTacToeSelectCellMove move = new TicTacToeSelectCellMove(player.getPlayer().getPlayerId(), TicTacToeCell.create(row, column));
        // Step 2. Performing actual TicTacToeMove
        return perform(player, move);
    }

    final public TicTacToeState bet(TicTacToePlayer player, int ammount) {
        // Step 1. Generating bet move
        TicTacToeMove move = new TicTacToeBetOnCellMove(player.getPlayer().getPlayerId(), ammount);
        // Step 2. Performing actual TicTacToeMove
        return perform(player, move);
    }

}
