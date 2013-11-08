package com.clemble.casino.android.player.service;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import org.springframework.web.client.RestTemplate;

import com.clemble.casino.player.security.PlayerToken;
import com.clemble.casino.player.service.PlayerRegistrationService;
import com.clemble.casino.player.web.PlayerLoginRequest;
import com.clemble.casino.player.web.PlayerRegistrationRequest;
import com.clemble.casino.player.web.PlayerSocialRegistrationRequest;
import com.clemble.casino.web.management.ManagementWebMapping;

public class AndroidPlayerRegistrationService implements PlayerRegistrationService {

    final private String managementUrl;
    final private RestTemplate restTemplate;

    public AndroidPlayerRegistrationService(String managementUrl, RestTemplate restTemplate) {
        this.managementUrl = checkNotNull(managementUrl);
        this.restTemplate = checkNotNull(restTemplate);
    }

    @Override
    public PlayerToken login(PlayerLoginRequest playerLoginRequest) {
        return restTemplate.postForEntity(managementUrl + ManagementWebMapping.MANAGEMENT_PLAYER_LOGIN, playerLoginRequest, PlayerToken.class).getBody();
    }

    @Override
    public PlayerToken createPlayer(PlayerRegistrationRequest registrationRequest) {
        return restTemplate.postForEntity(managementUrl + ManagementWebMapping.MANAGEMENT_PLAYER_REGISTRATION, registrationRequest, PlayerToken.class).getBody();
    }

    @Override
    public PlayerToken createSocialPlayer(PlayerSocialRegistrationRequest socialConnectionData) {
        return restTemplate.postForEntity(managementUrl + ManagementWebMapping.MANAGEMENT_PLAYER_REGISTRATION_SOCIAL, socialConnectionData, PlayerToken.class).getBody();
    }

}
