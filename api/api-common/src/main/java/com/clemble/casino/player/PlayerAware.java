package com.clemble.casino.player;

import java.io.Serializable;

public interface PlayerAware extends Serializable {

    final public String DEFAULT_PLAYER = "casino";
    final public String JSON_ID = "player";

    public String getPlayer();

}
