package com.gogomaya.server.game.action;

import java.io.Serializable;

public interface GamePlayerIterator extends Serializable {

    public long next();

    public long current();

    public long[] getPlayers();

}
