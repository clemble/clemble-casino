package com.gogomaya.server.game.event.client;

public class MoveTimeoutSurrenderEvent extends SurrenderEvent {

    /**
     * Generated 10/06/13
     */
    private static final long serialVersionUID = -3052155086475447441L;

    public MoveTimeoutSurrenderEvent(long playerId) {
        super(playerId);
    }

}
