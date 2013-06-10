package com.gogomaya.server.game.event.client;

import com.gogomaya.server.event.AbstractClientEvent;
import com.gogomaya.server.game.GameTimeState;

abstract public class TimeBreachedEvent extends AbstractClientEvent {

    /**
     * 
     */
    private static final long serialVersionUID = 88907816207175336L;

    final private GameTimeState timeState;

    public TimeBreachedEvent(long playerId, GameTimeState timeState) {
        super(playerId);
        this.timeState = timeState;
    }

    public GameTimeState getTimeState() {
        return timeState;
    }
}
