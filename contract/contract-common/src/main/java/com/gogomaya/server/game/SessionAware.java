package com.gogomaya.server.game;

import java.io.Serializable;

public interface SessionAware extends Serializable {

    final long DEFAULT_SESSION = 0L;

    long getSession();

}
