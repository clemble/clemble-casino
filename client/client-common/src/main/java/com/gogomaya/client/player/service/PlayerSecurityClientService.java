package com.gogomaya.client.player.service;


public interface PlayerSecurityClientService {

    public <T> T signGet(String request);

    public <T, S> T signUpdate(S request);

}
