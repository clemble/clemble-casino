package com.gogomaya.server.game.action.tictactoe;

import java.util.Collection;
import java.util.Map.Entry;

import com.gogomaya.server.game.action.AbstractGameState;
import com.gogomaya.server.game.action.tictactoe.move.TicTacToeMove;

public class TicTacToeState extends AbstractGameState<TicTacToeMove, TicTacToePlayerState> {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = -3282042914639667829L;

    final private TicTacToeCellState[][] board;

    private Entry<Byte, Byte> activeCell;

    public TicTacToeState(final Collection<TicTacToePlayerState> playerState,
            final Collection<? extends TicTacToeMove> nextMoves,
            final Collection<? extends TicTacToeMove> madeMoves,
            final TicTacToeCellState[][] board,
            final Entry<Byte, Byte> activeCell) {
        super(playerState, nextMoves, madeMoves);

        this.board = board.clone();
        this.activeCell = activeCell;
    }

    public TicTacToeCellState[][] getBoard() {
        return board;
    }
    
    public boolean complete(){
        long owner = board[activeCell.getKey()][activeCell.getValue()].getOwner();
        // Step 1. Check vertical
        boolean complete = true;
        for(int i = 0; i < board.length; i++) {
            complete = complete && board[activeCell.getKey()][i].getOwner() == owner;
        }
        if(complete)
            return complete;
        // Step 2. Check horizontal
        complete = true;
        for(int i = 0; i < board.length; i++) {
            complete = complete && board[i][activeCell.getValue()].getOwner() == owner;
        }
        if(complete)
            return complete;
        // Step 3. Check diagonal
        for(int i = 0, j = 0; i < board.length; i++, j++)
            complete = complete && board[i][j].getOwner() == owner;
        if(complete)
            return complete;
        // Step 4. Check diagonal
        for(int i = 0, j = board.length - 1; i < board.length; i++, j--)
            complete = complete && board[i][j].getOwner() == owner;
        if(complete)
            return complete;

        return complete;
    }

    public boolean isOwned(Entry<Byte, Byte> activeCell) {
        return board[activeCell.getKey()][activeCell.getValue()].owned();
    }

    public Entry<Byte, Byte> getActiveCell() {
        return activeCell;
    }

    public void setActiveCell(Entry<Byte, Byte> cell) {
        this.activeCell = cell;
    }

    public void setActiveCellState(TicTacToeCellState cellState) {
        TicTacToeCellState[][] newBoard = board.clone();
        newBoard[activeCell.getKey()][activeCell.getValue()] = cellState;
    }

}
