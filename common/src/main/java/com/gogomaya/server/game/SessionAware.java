package com.gogomaya.server.game;

public interface SessionAware {

    final long DEFAULT_SESSION = 0L;

    long getSession();

}
