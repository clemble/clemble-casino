package com.clemble.casino.game.account;

import com.clemble.casino.player.PlayerAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GamePlayerAccount implements PlayerAware {

    /**
     * Generated
     */
    private static final long serialVersionUID = -1635859321208535243L;

    final private String player;

    private long moneyLeft;
    private long moneySpent;

    public GamePlayerAccount(final String player, final long moneyLeft) {
        this.player = player;
        this.moneyLeft = moneyLeft;
    }

    @JsonCreator
    public GamePlayerAccount(@JsonProperty(PlayerAware.JSON_ID) final String player,
            @JsonProperty("moneyLeft") final long moneyLeft,
            @JsonProperty("moneySpent") final long moneySpent) {
        this.player = player;
        this.moneyLeft = moneyLeft;
        this.moneySpent = moneySpent;
    }

    @Override
    public String getPlayer() {
        return player;
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
        return "PlayerState [player=" + player + ", money=" + moneyLeft + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (moneyLeft ^ (moneyLeft >>> 32));
        result = prime * result + (int) (moneySpent ^ (moneySpent >>> 32));
        result = prime * result + (int) (player != null ? player.hashCode() : 0);
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
        return player.equals(other.player);
    }

}
