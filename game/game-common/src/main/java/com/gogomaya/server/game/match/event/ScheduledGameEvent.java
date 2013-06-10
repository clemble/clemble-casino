package com.gogomaya.server.game.match.event;

import com.gogomaya.server.event.AbstractServerEvent;
import com.gogomaya.server.game.match.ScheduledGameAware;

abstract public class ScheduledGameEvent extends AbstractServerEvent implements ScheduledGameAware {

    /**
     * Generated 02/06/13
     */
    private static final long serialVersionUID = 3655906211189134238L;

    private long scheduledGameId;

    @Override
    public long getScheduledGameId() {
        return scheduledGameId;
    }

    public void setScheduledGameId(long scheduledGameId) {
        this.scheduledGameId = scheduledGameId;
    }
}