package com.gogomaya.server.game.action.tictactoe.move;

import com.gogomaya.server.game.action.GameMove;

public class TicTacToeMove implements GameMove {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = -8775703673463350464L;

    final private TicTacToeMoveType moveType;

    final private long playerId;

    public TicTacToeMove(final TicTacToeMoveType moveType, final long playerId) {
        this.moveType = moveType;
        this.playerId = playerId;
    }

    public TicTacToeMoveType getMoveType() {
        return moveType;
    }

    public long getPlayerId() {
        return playerId;
    }

}
