package com.gogomaya.client.player.service;


public interface PlayerSecurityClientService<T> {

    public T signGet(String request);

    public <S> T signUpdate(S request);

}
