package com.gogomaya.server.integration.client;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

import org.springframework.web.client.RestTemplate;

import com.gogomaya.server.player.PlayerProfile;
import com.gogomaya.server.player.security.PlayerCredential;
import com.gogomaya.server.player.security.PlayerIdentity;
import com.gogomaya.server.player.web.RegistrationRequest;

public class GogomayaRestClient implements GogomayaClient {
    
    final public static String PROFILE_CREATE_URL = "/registration/signin";
    
    final private RestTemplate restTemplate;
    final private String baseUrl;
    
    public GogomayaRestClient(final String baseUrl, final RestTemplate restTemplate) {
        this.restTemplate = checkNotNull(restTemplate);
        this.baseUrl = checkNotNull(baseUrl);
    }

    @Override
    public PlayerIdentity createProfile(PlayerProfile playerProfile) {
        // Step 1. Generating RegistrationRequest
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setPlayerProfile(playerProfile);
        registrationRequest.setPlayerCredential(new PlayerCredential()
            .setEmail(playerProfile.getFirstName() + "@" + "gmail.com")
            .setPassword(UUID.randomUUID().toString()));
        // Step 2. Creating new user in localhost
        return restTemplate.postForObject(baseUrl + PROFILE_CREATE_URL, registrationRequest, PlayerIdentity.class);
    }

}
