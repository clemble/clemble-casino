package com.gogomaya.server.game.session;

import java.io.Serializable;

public interface GameSessionAware<T extends GameSessionAware<T>> extends Serializable {

    public long getSessionId();

    public T setSessionId(long newSessionId);

}
