package com.gogomaya.server.game.active;

import java.util.List;

public interface ActivePlayerQueue {

    public Long isActive(long playerId);

    public boolean markActive(long playerId, long sessionId);

    public void markInActive(long playerId);

    public void markInActive(List<Long> playerIds);

}
