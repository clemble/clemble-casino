package com.gogomaya.server.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogomaya.server.player.PlayerAware;

public class GamePlayerAccount implements PlayerAware {

    /**
     * Generated
     */
    private static final long serialVersionUID = -1635859321208535243L;

    final private long playerId;

    private long moneyLeft;
    private long moneySpent;

    public GamePlayerAccount(final long playerId, final long moneyLeft) {
        this.playerId = playerId;
        this.moneyLeft = moneyLeft;
    }

    @JsonCreator
    public GamePlayerAccount(@JsonProperty("playerId") final long playerId,
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

    public long getMoneySpent() {
        return moneySpent;
    }

    @JsonIgnore
    public long fetchMoneyTotal() {
        return moneySpent + moneyLeft;
    }

    @Override
    public String toString() {
        return "PlayerState [player=" + playerId + ", money=" + moneyLeft + "]";
    }

}
