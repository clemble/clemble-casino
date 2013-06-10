package com.gogomaya.server.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogomaya.server.player.PlayerAware;

public class GamePlayerState implements PlayerAware {

    /**
     * Generated
     */
    private static final long serialVersionUID = -1635859321208535243L;

    final private long playerId;

    private long moneyLeft;
    private long moneySpent;

    public GamePlayerState(final long playerId, final long moneyLeft) {
        this.playerId = playerId;
        this.moneyLeft = moneyLeft;
    }

    @JsonCreator
    public GamePlayerState(@JsonProperty("playerId") final long playerId,
            @JsonProperty("moneyLeft") final long moneyLeft,
            @JsonProperty("moneySpent") final long moneySpent) {
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

    public void subMoneyLeft(long money) {
        this.moneyLeft = moneyLeft - money;
        this.moneySpent = moneySpent + money;
    }

    @Override
    public String toString() {
        return "PlayerState [player=" + playerId + ", money=" + moneyLeft + "]";
    }

    public long getMoneySpent() {
        return moneySpent;
    }

}
