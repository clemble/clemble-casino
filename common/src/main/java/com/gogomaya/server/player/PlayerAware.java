package com.gogomaya.server.player;

import java.io.Serializable;

public interface PlayerAware extends Serializable {

    final public long DEFAULT_PLAYER = 0L;

    long getPlayerId();

}
