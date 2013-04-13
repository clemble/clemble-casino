package com.gogomaya.server.game.tictactoe.action.move;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.game.action.impl.AbstractGameMove;

public class TicTacToeMove extends AbstractGameMove {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = -8775703673463350464L;

    final private TicTacToeMoveType moveType;

    public TicTacToeMove(final long playerId, final TicTacToeMoveType moveType) {
        super(playerId);
        this.moveType = checkNotNull(moveType);
    }

    public TicTacToeMoveType getMoveType() {
        return moveType;
    }

}
