package com.gogomaya.server.game.table;

import com.gogomaya.server.game.specification.GameSpecification;

public interface GameTableQueue {

    public Long poll(GameSpecification specification);

    public void add(long tableId, GameSpecification specification);

}
