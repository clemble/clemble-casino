package com.gogomaya.server.game.active;

import java.util.List;

public interface ActivePlayerQueue {

    public Long isActive(Long playerId);

    public boolean markActive(Long playerId, Long sessionId);

    public void markInActive(Long playerId);

    public void markInActive(List<Long> playerIds);

}
