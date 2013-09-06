package com.gogomaya.android.player.service;

import static com.gogomaya.utils.Preconditions.checkNotNull;

import org.springframework.web.client.RestTemplate;

import com.gogomaya.player.security.PlayerIdentity;
import com.gogomaya.player.service.PlayerRegistrationService;
import com.gogomaya.player.web.PlayerLoginRequest;
import com.gogomaya.player.web.PlayerRegistrationRequest;
import com.gogomaya.player.web.PlayerSocialRegistrationRequest;
import com.gogomaya.web.management.ManagementWebMapping;

public class AndroidPlayerRegistrationService implements PlayerRegistrationService {

    final private String managementUrl;
    final private RestTemplate restTemplate;

    public AndroidPlayerRegistrationService(String managementUrl, RestTemplate restTemplate) {
        this.managementUrl = checkNotNull(managementUrl);
        this.restTemplate = checkNotNull(restTemplate);
    }

    @Override
    public PlayerIdentity login(PlayerLoginRequest playerLoginRequest) {
        return restTemplate.postForEntity(managementUrl + ManagementWebMapping.MANAGEMENT_PLAYER_LOGIN, playerLoginRequest, PlayerIdentity.class).getBody();
    }

    @Override
    public PlayerIdentity createPlayer(PlayerRegistrationRequest registrationRequest) {
        return restTemplate.postForEntity(managementUrl + ManagementWebMapping.MANAGEMENT_PLAYER_REGISTRATION, registrationRequest, PlayerIdentity.class)
                .getBody();
    }

    @Override
    public PlayerIdentity createSocialPlayer(PlayerSocialRegistrationRequest socialConnectionData) {
        return restTemplate.postForEntity(managementUrl + ManagementWebMapping.MANAGEMENT_PLAYER_REGISTRATION_SOCIAL, socialConnectionData,
                PlayerIdentity.class).getBody();
    }

}
