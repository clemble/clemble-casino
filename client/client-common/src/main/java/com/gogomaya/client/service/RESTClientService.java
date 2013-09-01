package com.gogomaya.client.service;

import java.util.List;

public interface RESTClientService {

    public <T> T getForEntity(String prefix, String url, Class<T> responseType, Object... urlVariables);

    public <T> List<T> getForEntityList(String prefix, String url, Class<T> responseType, Object... urlVariables);

    public <T> T putForEntity(String prefix, String url, Object request, Class<T> responseType, Object... urlVariables);

    public <T> T postForEntity(String prefix, String url, Object request, Class<T> responseType, Object... urlVariables);

}
