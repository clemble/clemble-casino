package com.clemble.casino.game.event.client.surrender;

import com.clemble.casino.game.event.client.GameClientEvent;

abstract public class SurrenderEvent extends GameClientEvent {

    /**
     * Generated 10/06/2013
     */
    private static final long serialVersionUID = 4875966049653785672L;

    public SurrenderEvent(String player) {
        super(player);
    }

}
