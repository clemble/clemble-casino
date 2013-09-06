package com.gogomaya.client.service;

import java.util.List;

public interface RestClientService {

    public <T> T getForEntity(String url, Class<T> responseType, Object... urlVariables);

    public <T> List<T> getForEntityList(String url, Class<T> responseType, Object... urlVariables);

    public <T> T putForEntity(String url, Object request, Class<T> responseType, Object... urlVariables);

    public <T> T postForEntity(String url, Object request, Class<T> responseType, Object... urlVariables);

}
