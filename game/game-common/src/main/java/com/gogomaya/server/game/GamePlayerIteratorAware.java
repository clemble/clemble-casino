package com.gogomaya.server.game;

import com.gogomaya.server.ActionLatch;

public interface GamePlayerIteratorAware {

    public GamePlayerIterator getPlayerIterator();

    public GameState setPlayerIterator(GamePlayerIterator playerIterator);

    public ActionLatch getActionLatch();

}
