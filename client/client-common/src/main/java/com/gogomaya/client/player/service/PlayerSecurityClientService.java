package com.gogomaya.client.player.service;

import java.io.Serializable;

public interface PlayerSecurityClientService<T> {

    public T signGet(String request);

    public T signUpdate(Serializable request);

}
