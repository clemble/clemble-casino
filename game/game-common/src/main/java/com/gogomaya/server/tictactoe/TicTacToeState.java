package com.gogomaya.server.tictactoe;

import java.util.Arrays;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gogomaya.server.game.AbstractGameState;
import com.gogomaya.server.game.DrawOutcome;
import com.gogomaya.server.game.GameOutcome;
import com.gogomaya.server.game.GamePlayerState;
import com.gogomaya.server.game.PlayerWonOutcome;
import com.gogomaya.server.game.SequentialPlayerIterator;
import com.gogomaya.server.tictactoe.event.client.TicTacToeSelectCellEvent;

@JsonIgnoreProperties({ "winner", "activeUsers" })
public class TicTacToeState extends AbstractGameState {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = -3282042914639667829L;

    private TicTacToeCellState[][] board = new TicTacToeCellState[3][3];

    private TicTacToeCell activeCell;

    public TicTacToeState() {
        // Step 0. Filling the board with empty cell value
        for (TicTacToeCellState[] row : board) {
            Arrays.fill(row, TicTacToeCellState.DEFAULT_CELL_STATE);
        }
    }

    public TicTacToeState(final Collection<GamePlayerState> playersStates) {
        this();

        setPlayerStates(playersStates);
        setPlayerIterator(new SequentialPlayerIterator(playersStates));
        setNextMove(new TicTacToeSelectCellEvent(getPlayerIterator().current()));
    }

    public TicTacToeCell getActiveCell() {
        return activeCell;
    }

    public TicTacToeState setActiveCell(TicTacToeCell cell) {
        this.activeCell = cell;
        return this;
    }

    public TicTacToeCellState[][] getBoard() {
        return board;
    }

    public TicTacToeCellState getCellState(int row, int column) {
        return row >= 0 && row < board.length && column >= 0 && column < board.length ? board[row][column] : null;
    }

    public TicTacToeCellState getCellState(TicTacToeCell cell) {
        return getCellState(cell.getRow(), cell.getColumn());
    }

    public TicTacToeState setBoard(final TicTacToeCell cell, final TicTacToeCellState cellState) {
        this.board[cell.getRow()][cell.getColumn()] = cellState;
        return this;
    }

    public TicTacToeState setBoard(TicTacToeCellState[][] board) {
        this.board = board;
        return this;
    }

    @Override
    public boolean complete() {
        // Step 1. Check vertical
        return getOutcome() != null;
    }

    public GameOutcome calculate() {
        // Step 1. Checking if there is a single winner
        long winner = getWinner();
        if (winner != TicTacToeCellState.DEFAULT_OWNER) {
            return new PlayerWonOutcome(winner);
        }
        // Step 2. Checking if game ended in draw
        if (!canHaveWinner()) {
            return new DrawOutcome();
        }
        // Step 3. Returning default value
        for(GamePlayerState playerState: getPlayerStates()) {
            if(playerState.getMoneyLeft() == 0) {
                winner = getOpponents(playerState.getPlayerId()).iterator().next();
                return new PlayerWonOutcome(winner);
            }
        }
        return null;
    }

    private long getWinner() {
        long completnece[] = new long[8];
        Arrays.fill(completnece, -1L);
        // Checking rows
        completnece[0] = owner(board[0][0], board[0][1], board[0][2]);
        completnece[1] = owner(board[1][0], board[1][1], board[1][2]);
        completnece[2] = owner(board[2][0], board[2][1], board[2][2]);
        // Checking columns
        completnece[3] = owner(board[0][0], board[1][0], board[2][0]);
        completnece[4] = owner(board[0][1], board[1][1], board[2][1]);
        completnece[5] = owner(board[0][2], board[1][2], board[2][2]);
        // Checking diagonals
        completnece[6] = owner(board[0][0], board[1][1], board[2][2]);
        completnece[7] = owner(board[0][2], board[1][1], board[2][0]);
        // Step 2. If at least one complete game is complete
        for (long complete : completnece)
            if (complete != TicTacToeCellState.DEFAULT_OWNER)
                return complete;
        return TicTacToeCellState.DEFAULT_OWNER;
    }

    private long owner(TicTacToeCellState firstCell, TicTacToeCellState secondCell, TicTacToeCellState therdCell) {
        return (firstCell.getOwner() == secondCell.getOwner() && secondCell.getOwner() == therdCell.getOwner()) ? firstCell.getOwner()
                : TicTacToeCellState.DEFAULT_OWNER;
    }

    private boolean canHaveWinner() {
        return canHaveSingleOwner(board[0][0], board[0][1], board[0][2]) || canHaveSingleOwner(board[1][0], board[1][1], board[1][2])
                || canHaveSingleOwner(board[2][0], board[2][1], board[2][2])
                // Checking columns
                || canHaveSingleOwner(board[0][0], board[1][0], board[2][0]) || canHaveSingleOwner(board[0][1], board[1][1], board[2][1])
                || canHaveSingleOwner(board[0][2], board[1][2], board[2][2])
                // Checking diagonals
                || canHaveSingleOwner(board[0][0], board[1][1], board[2][2]) || canHaveSingleOwner(board[0][2], board[1][1], board[2][0]);
    }

    private boolean canHaveSingleOwner(TicTacToeCellState firstCell, TicTacToeCellState secondCell, TicTacToeCellState therdCell) {
        return (firstCell.getOwner() == secondCell.getOwner() && (firstCell.getOwner() == TicTacToeCellState.DEFAULT_OWNER || therdCell.getOwner() == TicTacToeCellState.DEFAULT_OWNER))
                || (secondCell.getOwner() == therdCell.getOwner() && (secondCell.getOwner() == TicTacToeCellState.DEFAULT_OWNER || firstCell.getOwner() == TicTacToeCellState.DEFAULT_OWNER))
                || (therdCell.getOwner() == firstCell.getOwner() && (therdCell.getOwner() == TicTacToeCellState.DEFAULT_OWNER || secondCell.getOwner() == TicTacToeCellState.DEFAULT_OWNER));
    }

}
