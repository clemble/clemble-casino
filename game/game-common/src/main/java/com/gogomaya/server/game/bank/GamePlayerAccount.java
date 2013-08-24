package com.gogomaya.server.game.bank;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (moneyLeft ^ (moneyLeft >>> 32));
        result = prime * result + (int) (moneySpent ^ (moneySpent >>> 32));
        result = prime * result + (int) (playerId ^ (playerId >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GamePlayerAccount other = (GamePlayerAccount) obj;
        if (moneyLeft != other.moneyLeft)
            return false;
        if (moneySpent != other.moneySpent)
            return false;
        if (playerId != other.playerId)
            return false;
        return true;
    }

}
