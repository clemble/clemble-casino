package com.gogomaya.android.service;

import java.util.List;

import com.gogomaya.client.service.RESTClientService;

public class AndroidRESTService implements RESTClientService {

    @Override
    public <T> T getForEntity(String prefix, String url, Class<T> responseType, Object... urlVariables) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> List<T> getForEntityList(String prefix, String url, Class<T> responseType, Object... urlVariables) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T putForEntity(String prefix, String url, Object request, Class<T> responseType, Object... urlVariables) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T postForEntity(String prefix, String url, Object request, Class<T> responseType, Object... urlVariables) {
        // TODO Auto-generated method stub
        return null;
    }

}
