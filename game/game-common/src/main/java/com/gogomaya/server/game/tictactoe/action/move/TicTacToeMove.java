package com.gogomaya.server.game.tictactoe.action.move;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gogomaya.server.game.action.impl.AbstractGameMove;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @Type(value = TicTacToeSelectCellMove.class, name = "select"),
    @Type(value = TicTacToeBetOnCellMove.class, name = "bet") 
})
public class TicTacToeMove extends AbstractGameMove {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = -8775703673463350464L;

    public TicTacToeMove(final long playerId) {
        super(playerId);
    }

}
