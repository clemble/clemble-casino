package com.gogomaya.android.service;

import static com.gogomaya.utils.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.android.player.service.AndroidPlayerSecurityService;
import com.gogomaya.client.service.RestClientService;

public class AndroidRESTService implements RestClientService {

    final private String baseUrl;
    final private RestTemplate restTemplate;
    final private AndroidPlayerSecurityService securityClientService;

    public AndroidRESTService(String baseUrl, RestTemplate restTemplate, AndroidPlayerSecurityService securityClientService) {
        this.baseUrl = checkNotNull(baseUrl);
        this.restTemplate = checkNotNull(restTemplate);
        this.securityClientService = checkNotNull(securityClientService);
    }

    @Override
    public <T> T getForEntity(String prefix, String url, Class<T> responseType, Object... urlVariables) {
        String fullURL = baseUrl + prefix + url;
        HttpEntity<?> request = securityClientService.signGet(fullURL);
        return restTemplate.exchange(fullURL, HttpMethod.GET, request, responseType, urlVariables).getBody();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> getForEntityList(String prefix, String url, Class<T> responseType, Object... urlVariables) {
        String fullURL = baseUrl + prefix + url;
        HttpEntity<?> request = securityClientService.signGet(fullURL);
        return restTemplate.exchange(fullURL, HttpMethod.GET, request, List.class, urlVariables).getBody();
    }

    @Override
    public <T> T putForEntity(String prefix, String url, Object requestObject, Class<T> responseType, Object... urlVariables) {
        HttpEntity<?> request = securityClientService.signUpdate(requestObject);
        return restTemplate.exchange(baseUrl + prefix + url, HttpMethod.PUT, request, responseType, urlVariables).getBody();
    }

    @Override
    public <T> T postForEntity(String prefix, String url, Object requestObject, Class<T> responseType, Object... urlVariables) {
        HttpEntity<?> request = securityClientService.signUpdate(requestObject);
        return restTemplate.exchange(baseUrl + prefix + url, HttpMethod.POST, request, responseType, urlVariables).getBody();
    }

}
