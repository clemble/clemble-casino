package com.gogomaya.server.game.tictactoe.action;

import java.util.Arrays;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.gogomaya.server.game.action.impl.AbstractGameState;
import com.gogomaya.server.game.action.move.GameMove;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeBetOnCellMove;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeSelectCellMove;
import com.google.common.collect.ImmutableList;

@JsonIgnoreProperties({ "winner", "activeUsers" })
public class TicTacToeState extends AbstractGameState {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = -3282042914639667829L;

    private TicTacToeCellState[][] board = new TicTacToeCellState[3][3];

    private TicTacToeCell activeCell;

    public TicTacToeState() {
        for (TicTacToeCellState[] row : board) {
            Arrays.fill(row, TicTacToeCellState.DEFAULT_CELL_STATE);
        }
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

    public TicTacToeState setBoard(TicTacToeCellState[][] board) {
        this.board = board;
        return this;
    }

    @Override
    public boolean complete() {
        // Step 1. Check vertical
        return getWinner() != -1L;
    }

    public long getWinner() {
        long completnece[] = new long[11];
        Arrays.fill(completnece, -1L);
        for (int i = 1; i < board.length; i++) {
            // Checking rows
            completnece[0] = board[0][i].getOwner() == board[0][i - 1].getOwner() ? board[0][i - 1].getOwner() : TicTacToeCellState.DEFAULT_OWNER;
            completnece[1] = board[1][i].getOwner() == board[1][i - 1].getOwner() ? board[1][i - 1].getOwner() : TicTacToeCellState.DEFAULT_OWNER;
            completnece[2] = board[2][i].getOwner() == board[2][i - 1].getOwner() ? board[2][i].getOwner() : TicTacToeCellState.DEFAULT_OWNER;
            // Checking columns
            completnece[3] = board[i][0].getOwner() == board[i - 1][0].getOwner() ? board[i][0].getOwner() : TicTacToeCellState.DEFAULT_OWNER;
            completnece[4] = board[i][1].getOwner() == board[i - 1][1].getOwner() ? board[i][1].getOwner() : TicTacToeCellState.DEFAULT_OWNER;
            completnece[5] = board[i][2].getOwner() == board[i - 1][2].getOwner() ? board[i][2].getOwner() : TicTacToeCellState.DEFAULT_OWNER;
            // Checking diagonals
            completnece[6] = board[i - 1][i - 1].getOwner() == board[i][i].getOwner() ? board[i - 1][i - 1].getOwner() : TicTacToeCellState.DEFAULT_OWNER;
            completnece[7] = board[3 - i][3 - i].getOwner() == board[2 - i][2 - i].getOwner() ? board[3 - i][3 - i].getOwner() : TicTacToeCellState.DEFAULT_OWNER;
        }
        // Step 2. If at least one complete game is complete
        for (long complete : completnece)
            if (complete != -1L)
                return complete;
        return -1L;
    }

    public boolean isOwned(TicTacToeCell activeCell) {
        return board[activeCell.getRow()][activeCell.getColumn()].owned();
    }

    public TicTacToeCell getActiveCell() {
        return activeCell;
    }

    public void setActiveCell(TicTacToeCell cell) {
        this.activeCell = cell;
    }

    public void setActiveCellState(TicTacToeCellState cellState) {
        TicTacToeCellState[][] newBoard = board.clone();
        newBoard[activeCell.getRow()][activeCell.getColumn()] = cellState;
    }

    public TicTacToeState setNextMoveSelect(long playerId) {
        if (getPlayerIterator().contains(playerId)) {
            setNextMove(new TicTacToeSelectCellMove(playerId));
            setActiveCell(TicTacToeCell.DEFAULT);
            cleanMadeMove();
        }
        return this;
    }

    public void setNextMoveBet() {
        long[] players = getPlayerIterator().getPlayers();
        setNextMoves(ImmutableList.<GameMove> of(new TicTacToeBetOnCellMove(players[0]), new TicTacToeBetOnCellMove(players[1])));
        cleanMadeMove();
    }

}
