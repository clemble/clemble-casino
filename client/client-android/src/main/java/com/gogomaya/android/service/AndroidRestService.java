package com.gogomaya.android.service;

import static com.gogomaya.utils.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogomaya.client.player.service.PlayerSecurityClientService;
import com.gogomaya.client.service.RestClientService;

public class AndroidRestService implements RestClientService {

    final private String baseUrl;
    final private RestTemplate restTemplate;
    final private PlayerSecurityClientService<HttpEntity<?>> securityClientService;

    public AndroidRestService(String baseUrl, RestTemplate restTemplate, PlayerSecurityClientService<HttpEntity<?>> securityClientService) {
        this.securityClientService = securityClientService;
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public AndroidRestService(String baseUrl, ObjectMapper objectMapper, PlayerSecurityClientService<HttpEntity<?>> securityClientService) {
        this.baseUrl = checkNotNull(baseUrl);
        this.securityClientService = checkNotNull(securityClientService);

        this.restTemplate = checkNotNull(new RestTemplate());

        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(jackson2HttpMessageConverter);

        this.restTemplate.setMessageConverters(messageConverters);

    }

    @Override
    public <T> T getForEntity(String url, Class<T> responseType, Object... urlVariables) {
        String fullURL = baseUrl + url;
        HttpEntity<?> request = securityClientService.signGet(fullURL);
        return restTemplate.exchange(fullURL, HttpMethod.GET, request, responseType, urlVariables).getBody();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> getForEntityList(String url, Class<T> responseType, Object... urlVariables) {
        String fullURL = baseUrl + url;
        HttpEntity<?> request = securityClientService.signGet(fullURL);
        return restTemplate.exchange(fullURL, HttpMethod.GET, request, List.class, urlVariables).getBody();
    }

    @Override
    public <T> T putForEntity(String url, Object requestObject, Class<T> responseType, Object... urlVariables) {
        HttpEntity<?> request = securityClientService.signUpdate(requestObject);
        return restTemplate.exchange(baseUrl + url, HttpMethod.PUT, request, responseType, urlVariables).getBody();
    }

    @Override
    public <T> T postForEntity(String url, Object requestObject, Class<T> responseType, Object... urlVariables) {
        HttpEntity<?> request = securityClientService.signUpdate(requestObject);
        return restTemplate.exchange(baseUrl + url, HttpMethod.POST, request, responseType, urlVariables).getBody();
    }

    @Override
    public RestClientService construct(String baseUrl) {
        return new AndroidRestService(baseUrl, restTemplate, securityClientService);
    }

}
