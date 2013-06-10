package com.gogomaya.server.game.event.client;

import com.gogomaya.server.game.GameTimeState;

public class TotalTimeBreachedEvent extends TimeBreachedEvent {

    /**
     * 
     */
    private static final long serialVersionUID = 6999945454488627240L;

    public TotalTimeBreachedEvent(long playerId, GameTimeState timeState) {
        super(playerId, timeState);
    }

}
