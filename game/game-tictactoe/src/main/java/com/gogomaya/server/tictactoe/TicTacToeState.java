package com.gogomaya.server.tictactoe;

import java.util.Arrays;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.server.game.GamePlayerState;
import com.gogomaya.server.game.SequentialPlayerIterator;
import com.gogomaya.server.game.cell.Cell;
import com.gogomaya.server.game.cell.CellState;
import com.gogomaya.server.game.outcome.DrawOutcome;
import com.gogomaya.server.game.outcome.GameOutcome;
import com.gogomaya.server.game.outcome.PlayerWonOutcome;
import com.gogomaya.server.player.PlayerAware;

@JsonIgnoreProperties({ "activeUsers" })
@JsonTypeName("ticTacToe")
public class TicTacToeState extends AbstractPicPacPoeGameState {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = -3282042914639667829L;

    private CellState[][] board = new CellState[3][3];

    private Cell selected;

    public TicTacToeState() {
        // Step 0. Filling the board with empty cell value
        for (CellState[] row : board) {
            Arrays.fill(row, CellState.DEFAULT);
        }
    }

    public TicTacToeState(final Collection<GamePlayerState> playersStates) {
        this();

        setPlayerStates(playersStates);
        setPlayerIterator(new SequentialPlayerIterator(playersStates));
        setSelectNext();
    }

    public Cell getSelected() {
        return selected;
    }

    public TicTacToeState setSelected(Cell cell) {
        this.selected = cell;
        return this;
    }

    public CellState[][] getBoard() {
        return board;
    }

    public TicTacToeState setSelectedState(final CellState cellState) {
        this.board[selected.getRow()][selected.getColumn()] = cellState;
        setSelected(Cell.DEFAULT);
        return this;
    }

    public TicTacToeState setBoard(CellState[][] board) {
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
        long winner = hasWinner();
        if (winner != PlayerAware.DEFAULT_PLAYER) {
            return new PlayerWonOutcome(winner);
        }
        // Step 2. Checking if game ended in draw
        if (!canHaveWinner()) {
            return new DrawOutcome();
        }
        // Step 3. Returning default value
        for (GamePlayerState playerState : getPlayerStates()) {
            if (playerState.getMoneyLeft() == 0) {
                winner = getOpponents(playerState.getPlayerId()).iterator().next();
                return new PlayerWonOutcome(winner);
            }
        }
        return null;
    }

    private long hasWinner() {
        return owner(board[0][0], board[0][1], board[0][2]) // Checking rows
                + owner(board[1][0], board[1][1], board[1][2])
                + owner(board[2][0], board[2][1], board[2][2])
                + owner(board[0][0], board[1][0], board[2][0]) // Checking columns
                + owner(board[0][1], board[1][1], board[2][1])
                + owner(board[0][2], board[1][2], board[2][2]) // Checking diagonals
                + owner(board[0][0], board[1][1], board[2][2])
                + owner(board[0][2], board[1][1], board[2][0]);
    }

    private long owner(CellState firstCell, CellState secondCell, CellState therdCell) {
        return (firstCell.getOwner() == secondCell.getOwner() && secondCell.getOwner() == therdCell.getOwner())
                ? firstCell.getOwner()
                : PlayerAware.DEFAULT_PLAYER;
    }

    private boolean canHaveWinner() {
        return canHaveSingleOwner(board[0][0], board[0][1], board[0][2])
                || canHaveSingleOwner(board[1][0], board[1][1], board[1][2])
                || canHaveSingleOwner(board[2][0], board[2][1], board[2][2])
                // Checking columns
                || canHaveSingleOwner(board[0][0], board[1][0], board[2][0])
                || canHaveSingleOwner(board[0][1], board[1][1], board[2][1])
                || canHaveSingleOwner(board[0][2], board[1][2], board[2][2])
                // Checking diagonals
                || canHaveSingleOwner(board[0][0], board[1][1], board[2][2])
                || canHaveSingleOwner(board[0][2], board[1][1], board[2][0]);
    }

    private boolean canHaveSingleOwner(CellState firstCell, CellState secondCell, CellState therdCell) {
        // In arbitrary line XOX if X is free, it can be owned by O, if O is free it also can be owned by X
        return (firstCell.getOwner() == secondCell.getOwner() && (!firstCell.owned() || !therdCell.owned()))
                || (secondCell.getOwner() == therdCell.getOwner() && (!secondCell.owned() || !firstCell.owned()))
                || (therdCell.getOwner() == firstCell.getOwner() && (!therdCell.owned() || !secondCell.owned()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((selected == null) ? 0 : selected.hashCode());
        result = prime * result + Arrays.hashCode(board);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TicTacToeState other = (TicTacToeState) obj;
        if (selected == null) {
            if (other.selected != null)
                return false;
        } else if (!selected.equals(other.selected))
            return false;
        if (!Arrays.deepEquals(board, other.board))
            return false;
        return true;
    }

}
