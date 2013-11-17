package com.clemble.casino.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.web.client.RestTemplate;

import com.clemble.casino.player.security.PlayerToken;
import com.clemble.casino.player.service.PlayerRegistrationService;
import com.clemble.casino.player.web.PlayerLoginRequest;
import com.clemble.casino.player.web.PlayerRegistrationRequest;
import com.clemble.casino.player.web.PlayerSocialRegistrationRequest;
import com.clemble.casino.web.management.ManagementWebMapping;

public class IntegrationPlayerRegistrationService implements PlayerRegistrationService {

    final private String baseUrl;
    final private RestTemplate restTemplate;

    public IntegrationPlayerRegistrationService(String baseUrl, RestTemplate restTemplate) {
        this.baseUrl = checkNotNull(baseUrl);
        this.restTemplate = checkNotNull(restTemplate);
    }

    @Override
    public PlayerToken login(PlayerLoginRequest playerLoginRequest) {
        // Step 1. Generating url
        String url = baseUrl + ManagementWebMapping.MANAGEMENT_PLAYER_LOGIN;
        // Step 2. Requesting through Rest
        return restTemplate.postForObject(url, playerLoginRequest, PlayerToken.class);
    }

    @Override
    public PlayerToken createPlayer(PlayerRegistrationRequest registrationRequest) {
        // Step 1. Generating url
        String url = baseUrl + ManagementWebMapping.MANAGEMENT_PLAYER_REGISTRATION;
        // Step 2. Making rest request
        return restTemplate.postForObject(url, registrationRequest, PlayerToken.class);
    }

    @Override
    public PlayerToken createSocialPlayer(PlayerSocialRegistrationRequest socialConnectionData) {
        // Step 1. Generating url
        String url = baseUrl + ManagementWebMapping.MANAGEMENT_PLAYER_REGISTRATION_SOCIAL;
        // Step 2. Making rest request
        return restTemplate.postForObject(url, socialConnectionData, PlayerToken.class);
    }

}
