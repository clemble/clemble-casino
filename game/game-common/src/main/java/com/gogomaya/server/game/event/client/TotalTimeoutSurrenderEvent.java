package com.gogomaya.server.game.event.client;

public class TotalTimeoutSurrenderEvent extends SurrenderEvent {

    /**
     * Generated 10/06/13
     */
    private static final long serialVersionUID = 6999945454488627240L;

    public TotalTimeoutSurrenderEvent(long playerId) {
        super(playerId);
    }

}
