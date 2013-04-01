package com.gogomaya.server.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

import org.springframework.web.client.RestTemplate;

import com.gogomaya.server.player.PlayerProfile;
import com.gogomaya.server.player.security.PlayerCredential;
import com.gogomaya.server.player.security.PlayerIdentity;
import com.gogomaya.server.player.web.RegistrationRequest;

public class RestPlayerOperations implements PlayerOperations {

    final private static String CREATE_URL = "/spi/registration/signin";

    final private String baseUrl;
    final private RestTemplate restTemplate;

    public RestPlayerOperations(final String baseUrl, final RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public Player createPlayer(PlayerProfile playerProfile) {
        // Step 0. Sanity check
        checkNotNull(playerProfile);
        // Step 1. Creating RegistrationRequest for processing
        PlayerCredential playerCredential = new PlayerCredential().setEmail(UUID.randomUUID().toString() + "@gmail.com").setPassword(
                UUID.randomUUID().toString());
        RegistrationRequest registrationRequest = new RegistrationRequest().setPlayerProfile(playerProfile).setPlayerCredential(playerCredential);
        // Step 2. Forwarding to appropriate method for processing
        return createPlayer(registrationRequest);
    }

    @Override
    public Player createPlayer(RegistrationRequest registrationRequest) {
        // Step 0. Sanity check
        checkNotNull(registrationRequest);
        // Step 1. Performing actual player creation
        PlayerIdentity playerIdentity = restTemplate.postForObject(baseUrl + CREATE_URL, registrationRequest, PlayerIdentity.class);
        checkNotNull(playerIdentity);
        // Step 2. Generating Player from created request
        return new Player().setPlayerId(playerIdentity.getPlayerId()).setIdentity(playerIdentity).setProfile(registrationRequest.getPlayerProfile())
                .setCredential(registrationRequest.getPlayerCredential());
    }

}
