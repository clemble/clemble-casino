package com.gogomaya.client.player.service;

import com.gogomaya.player.PlayerAware;

public interface PlayerSecurityClientService<T> extends PlayerAware {

    public <S> T signCreate(S request);

    public T signRead(String request);

    public <S> T signUpdate(S request);

    public T signDelete(String request);

}
