package com.clemble.casino.game;

import java.io.Serializable;

public interface SessionAware extends Serializable {

    final GameSessionKey DEFAULT_SESSION = new GameSessionKey();

    GameSessionKey getSession();

}
