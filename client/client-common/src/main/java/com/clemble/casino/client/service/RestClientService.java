package com.clemble.casino.client.service;

import java.util.List;

import com.clemble.casino.player.PlayerAware;

public interface RestClientService extends PlayerAware {

    public <T> T getForEntity(CharSequence url, Class<T> responseType, Object... urlVariables);

    public <T> List<T> getForEntityList(CharSequence url, Class<T> responseType, Object... urlVariables);

    public <T> T putForEntity(CharSequence url, Object request, Class<T> responseType, Object... urlVariables);

    public <T> T postForEntity(CharSequence url, Object request, Class<T> responseType, Object... urlVariables);

    public <T> T deleteForEntity(CharSequence url, Class<T> responseType, Object... urlVariable);

    public RestClientService construct(CharSequence baseUrl);

}
