package com.gogomaya.player.service;

import java.io.Serializable;

public interface PlayerSecurityService<T> {

    public T signGet(String request);

    public T signUpdate(Serializable request);

}
