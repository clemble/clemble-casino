package com.gogomaya.server.player;

import java.io.Serializable;

public interface PlayerAware<T extends PlayerAware<T>> extends Serializable {

    long getPlayerId();

    T setPlayerId(long playerId);

}
