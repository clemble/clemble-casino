package com.gogomaya.server.game.event.client;

import com.gogomaya.server.game.GameTimeState;

public class MoveTimeBreachedEvent extends TimeBreachedEvent {

    /**
     * 
     */
    private static final long serialVersionUID = -3052155086475447441L;

    public MoveTimeBreachedEvent(long playerId, GameTimeState timeState) {
        super(playerId, timeState);
    }

}
