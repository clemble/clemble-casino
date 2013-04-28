package com.gogomaya.server.game.tictactoe.action.move;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;

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

    public TicTacToeMove(final int moveId, final long playerId) {
        super(moveId, playerId);
    }

}
