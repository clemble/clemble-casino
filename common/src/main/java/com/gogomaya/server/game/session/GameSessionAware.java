package com.gogomaya.server.game.session;

import java.io.Serializable;

public interface GameSessionAware<T extends GameSessionAware<T>> extends Serializable {

    public String getSessionId();

    public T sesSessionId(String newSessionId);

}
