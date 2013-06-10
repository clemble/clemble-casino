package com.gogomaya.server.game.event.client;

import com.gogomaya.server.event.AbstractClientEvent;

abstract public class SurrenderEvent extends AbstractClientEvent {

    /**
     * Generated 10/06/2013
     */
    private static final long serialVersionUID = 4875966049653785672L;

    public SurrenderEvent(long playerId) {
        super(playerId);
    }

}
