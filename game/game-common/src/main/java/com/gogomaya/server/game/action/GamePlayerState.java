package com.gogomaya.server.game.action;

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

    @JsonCreator
    public GamePlayerState(
            @JsonProperty("playerId") final long playerId,
            @JsonProperty("moneyLeft") final long moneyLeft) {
        this.playerId = playerId;
        this.moneyLeft = moneyLeft;
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
    }

    @Override
    public String toString() {
        return "PlayerState [player=" + playerId + ", money=" + moneyLeft + "]";
    }

}
