package com.gogomaya.server.game.table;

import com.gogomaya.server.game.specification.GameSpecification;

public interface PendingSessionQueue {

    public Long poll(GameSpecification specification);

    public void add(long session, GameSpecification specification);

    public void invalidate(long session, GameSpecification specification);

}
