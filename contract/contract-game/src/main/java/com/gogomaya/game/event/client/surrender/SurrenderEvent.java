package com.gogomaya.game.event.client.surrender;

import com.gogomaya.game.event.client.GameClientEvent;

abstract public class SurrenderEvent extends GameClientEvent {

    /**
     * Generated 10/06/2013
     */
    private static final long serialVersionUID = 4875966049653785672L;

    public SurrenderEvent(long playerId) {
        super(playerId);
    }

}
