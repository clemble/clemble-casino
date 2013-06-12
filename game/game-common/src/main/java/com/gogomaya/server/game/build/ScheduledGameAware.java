package com.gogomaya.server.game.build;

import java.io.Serializable;

public interface ScheduledGameAware extends Serializable {

    public long getScheduledGameId();

}
