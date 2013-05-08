package com.gogomaya.server.game.tictactoe.action;

import java.util.Arrays;
import java.util.Collection;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.action.GamePlayerState;
import com.gogomaya.server.game.action.SequentialPlayerIterator;
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
    }

    public TicTacToeState(final Collection<GamePlayerState> playersStates) {
        // Step 0. Filling the board with empty cell value
        for (TicTacToeCellState[] row : board) {
            Arrays.fill(row, TicTacToeCellState.DEFAULT_CELL_STATE);
        }

        setPlayerStates(playersStates);
        setPlayerIterator(new SequentialPlayerIterator(playersStates));
        setNextMove(new TicTacToeSelectCellMove(getPlayerIterator().current()));
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

    public TicTacToeState setBoard(TicTacToeCellState[][] board) {
        this.board = board;
        return this;
    }

    @Override
    public boolean complete() {
        // Step 1. Check vertical
        return getWinner() != -1L;
    }

    @Override
    public long getWinner() {
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
            if (complete != -1L)
                return complete;
        return -1L;
    }

    private long owner(TicTacToeCellState firstCell, TicTacToeCellState secondCell, TicTacToeCellState therdCell) {
        return (firstCell.getOwner() == secondCell.getOwner() && secondCell.getOwner() == therdCell.getOwner()) ? firstCell.getOwner()
                : TicTacToeCellState.DEFAULT_OWNER;
    }

    @Override
    final protected TicTacToeState apply(final GameMove move) {
        // Step 1. Processing Select cell move
        if (move instanceof TicTacToeSelectCellMove) {
            return processSelectCellMove((TicTacToeSelectCellMove) move);
        } else if (move instanceof TicTacToeBetOnCellMove) {
            return processBetOnCellMove((TicTacToeBetOnCellMove) move);
        }
        // Step 2. Returning default state
        throw GogomayaException.create(GogomayaError.GamePlayMoveNotSupported);
    }

    private TicTacToeState processBetOnCellMove(final TicTacToeBetOnCellMove betMove) {
        getPlayerState(betMove.getPlayerId()).subMoneyLeft(betMove.getBet());
        addMadeMove(betMove);

        if (getNextMoves().isEmpty()) {
            long[] players = getPlayerIterator().getPlayers();

            long firstPlayerBet = ((TicTacToeBetOnCellMove) getMadeMove(players[0])).getBet();
            long secondPlayerBet = ((TicTacToeBetOnCellMove) getMadeMove(players[1])).getBet();

            board[activeCell.getRow()][activeCell.getColumn()] = (firstPlayerBet == secondPlayerBet) ? new TicTacToeCellState(0L, firstPlayerBet,
                    secondPlayerBet) : new TicTacToeCellState(firstPlayerBet > secondPlayerBet ? players[0] : players[1], firstPlayerBet, secondPlayerBet);
            activeCell = TicTacToeCell.DEFAULT;

            setNextMove(new TicTacToeSelectCellMove(getPlayerIterator().next()));
            cleanMadeMove();
        }

        return this;
    }

    private TicTacToeState processSelectCellMove(final TicTacToeSelectCellMove selectCellMove) {
        TicTacToeCell cellToSelect = selectCellMove.getCell();
        // Step 1. Sanity check
        if (board[cellToSelect.getRow()][cellToSelect.getColumn()].owned()) {
            throw GogomayaException.create(GogomayaError.TicTacToeCellOwned);
        }
        // Step 2. Generating next moves
        activeCell = cellToSelect;
        long[] players = getPlayerIterator().getPlayers();
        setNextMoves(ImmutableList.<GameMove> of(new TicTacToeBetOnCellMove(players[0]), new TicTacToeBetOnCellMove(players[1])));
        cleanMadeMove();
        // Step 3. Returning result
        return this;
    }

}
