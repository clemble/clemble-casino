package com.gogomaya.server.game.active;

public interface PlayerStateManager {

    public Long isActive(long playerId);

    public boolean markAvailable(long playerId);

    public boolean markBusy(long playerId, long sessionId);

}
