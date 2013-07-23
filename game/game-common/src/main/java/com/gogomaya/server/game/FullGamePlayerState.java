package com.gogomaya.server.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;

public class FullGamePlayerState extends GamePlayerState {

    /**
     * Generated
     */
    private static final long serialVersionUID = -1635859321208535243L;

    final private long playerId;
    final private long moneyLeft;
    final private long moneySpent;

    public FullGamePlayerState(final long playerId, final long moneyLeft) {
        super(playerId, moneyLeft, 0);
        this.playerId = playerId;
        this.moneyLeft = moneyLeft;
        this.moneySpent = 0;
    }

    @JsonCreator
    public FullGamePlayerState(@JsonProperty("playerId") final long playerId,
            @JsonProperty("moneyLeft") final long moneyLeft,
            @JsonProperty("moneySpent") final long moneySpent) {
        super(playerId, moneyLeft, moneySpent);
        this.playerId = playerId;
        this.moneyLeft = moneyLeft;
        this.moneySpent = moneySpent;
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

    public long getMoneyLeft() {
        return moneyLeft;
    }

    public long getMoneySpent() {
        return moneySpent;
    }

    public long getMoneyTotal() {
        return moneySpent + moneyLeft;
    }

    public FullGamePlayerState subtract(long money) {
        if (money > moneyLeft)
            throw GogomayaException.fromError(GogomayaError.GamePlayBetOverflow);
        return new FullGamePlayerState(playerId, moneyLeft - money, moneySpent + money);
    }

}
