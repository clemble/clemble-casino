package com.gogomaya.server.game.action.move;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogomaya.server.player.PlayerAware;

public class GiveUpMove implements PlayerAware, GameMove {

    /**
     * Generated 29/05/13
     */
    private static final long serialVersionUID = 4501169964446540650L;

    final private long playerId;

    @JsonCreator
    public GiveUpMove(@JsonProperty("player") long playerId) {
        this.playerId = playerId;
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

}
