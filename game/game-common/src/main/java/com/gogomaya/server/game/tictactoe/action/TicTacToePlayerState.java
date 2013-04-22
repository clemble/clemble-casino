package com.gogomaya.server.game.tictactoe.action;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.gogomaya.server.game.action.impl.AbstractGamePlayerState;

public class TicTacToePlayerState extends AbstractGamePlayerState {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = 250689205093466915L;

    private long moneyLeft;

    @JsonCreator
    public TicTacToePlayerState(@JsonProperty("playerId") final long playerId,
            @JsonProperty("moneyLeft") final long moneyLeft) {
        super(playerId);
        this.moneyLeft = moneyLeft;
    }

    public long getMoneyLeft() {
        return moneyLeft;
    }

    public void subMoneyLeft(long money) {
        if (money > moneyLeft)
            throw new IllegalArgumentException("Not enough money " + moneyLeft + " from " + money);
        moneyLeft -= money;
    }

}
