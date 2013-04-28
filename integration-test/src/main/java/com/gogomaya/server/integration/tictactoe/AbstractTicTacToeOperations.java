package com.gogomaya.server.integration.tictactoe;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.gogomaya.server.game.configuration.GameSpecificationOptions;
import com.gogomaya.server.game.configuration.SelectSpecificationOptions;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.tictactoe.action.TicTacToeCell;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.game.tictactoe.action.TicTacToeTable;
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
    final private GameOperations gameOperations;
    final private GameListenerOperations<TicTacToeTable> tableListenerOperations;

    public AbstractTicTacToeOperations(PlayerOperations playerOperations, final GameOperations gameOperations, final GameListenerOperations<TicTacToeTable> tableListenerOperations){
        this.playerOperations = checkNotNull(playerOperations);
        this.gameOperations = checkNotNull(gameOperations);
        this.tableListenerOperations = checkNotNull(tableListenerOperations);
    }
    
    
    final protected GameSpecification selectSpecification() {
        GameSpecificationOptions specificationOptions = gameOperations.getOptions();
        if (specificationOptions instanceof SelectSpecificationOptions) {
            return ((SelectSpecificationOptions) specificationOptions).getSpecifications().get(0);
        } else {
            throw new IllegalArgumentException();
        }
    }

    final public List<TicTacToePlayer> start() {
        // Step 1. Selecting specification for the game
        GameSpecification specification = selectSpecification();
        // Step 2. Creating user and trying to put them on the same table
        TicTacToePlayer playerA = start(specification);
        TicTacToePlayer playerB = start(specification);
        while (playerA.getTable().getTableId() != playerB.getTable().getTableId()) {
            playerA.getListenerControl().stopListener();
            playerA = start(specification);
            // waits added to be sure everyone on the same page
            if (playerA.getTable().getTableId() != playerB.getTable().getTableId()) {
                playerB.getListenerControl().stopListener();
                playerB = start(specification);
            }
        }
        TicTacToePlayerUtils.syncVersions(playerA, playerB);
        // Step 3. Returning generated value who ever goes first is choosen as first
        TicTacToeState state = playerB.getTable().getState() != null ? playerB.getTable().getState() : playerA.getTable().getState();
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
        final TicTacToeTable table = (TicTacToeTable) checkNotNull(gameOperations.start(player, specification));
        final TicTacToePlayer toePlayer = new TicTacToePlayer(this);
        toePlayer.setPlayer(player);
        toePlayer.setTable(table);
        // Step 3. Creating listener, that will update GameListener
        toePlayer.setListenerControl(tableListenerOperations.listen(table, new GameListener<TicTacToeTable>() {

            @Override
            public void updated(TicTacToeTable gameTable) {
                toePlayer.setTable(gameTable);
            }

        }));
        // Step 3. Creating player configurations
        return toePlayer;
    }

    final public void select(TicTacToePlayer player, int row, int column) {
        // Step 1. Generating bet move
        TicTacToeSelectCellMove move = new TicTacToeSelectCellMove(player.getPlayer().getPlayerId(), TicTacToeCell.create(row, column));
        // Step 2. Performing actual TicTacToeMove
        perform(player, move);
    }

    final public void bet(TicTacToePlayer player, int ammount) {
        // Step 1. Generating bet move
        TicTacToeMove move = new TicTacToeBetOnCellMove(player.getPlayer().getPlayerId(), ammount);
        // Step 2. Performing actual TicTacToeMove
        perform(player, move);
    }

}
