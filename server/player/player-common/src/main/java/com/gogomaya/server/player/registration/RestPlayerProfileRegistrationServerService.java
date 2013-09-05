package com.gogomaya.server.player.registration;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.web.client.RestTemplate;

import com.gogomaya.player.PlayerProfile;
import com.gogomaya.player.SocialConnectionData;
import com.gogomaya.web.player.PlayerWebMapping;

public class RestPlayerProfileRegistrationServerService implements PlayerProfileRegistrationServerService {

    final private String baseUrl;
    final private RestTemplate restTemplate;

    public RestPlayerProfileRegistrationServerService(String baseUrl, RestTemplate restTemplate) {
        this.baseUrl = checkNotNull(baseUrl);
        this.restTemplate = checkNotNull(restTemplate);
    }

    @Override
    public PlayerProfile createPlayerProfile(PlayerProfile playerProfile) {
        return restTemplate.postForEntity(baseUrl + PlayerWebMapping.PLAYER_PREFIX + PlayerWebMapping.PLAYER_PROFILE_REGISTRATION, playerProfile, PlayerProfile.class).getBody();
    }

    @Override
    public PlayerProfile createPlayerProfile(SocialConnectionData socialConnectionData) {
        return restTemplate.postForEntity(baseUrl + PlayerWebMapping.PLAYER_PREFIX + PlayerWebMapping.PLAYER_PROFILE_REGISTRATION_SOCIAL, socialConnectionData, PlayerProfile.class).getBody();
    }

}
