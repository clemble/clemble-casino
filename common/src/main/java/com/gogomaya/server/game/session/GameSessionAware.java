package com.gogomaya.server.game.session;

import java.io.Serializable;

public interface GameSessionAware<T extends GameSessionAware<T>> extends Serializable {

    public Long getSessionId();

    public T setSessionId(Long newSessionId);

}
