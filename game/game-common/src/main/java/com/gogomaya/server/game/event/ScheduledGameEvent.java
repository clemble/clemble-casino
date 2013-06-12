package com.gogomaya.server.game.event;

import com.gogomaya.server.game.build.ScheduledGameAware;

abstract public class ScheduledGameEvent implements ScheduledGameAware {

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