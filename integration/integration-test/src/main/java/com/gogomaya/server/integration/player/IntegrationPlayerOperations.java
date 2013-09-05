package com.gogomaya.server.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.web.client.RestTemplate;

import com.gogomaya.player.security.PlayerIdentity;
import com.gogomaya.player.web.PlayerLoginRequest;
import com.gogomaya.player.web.PlayerRegistrationRequest;
import com.gogomaya.server.integration.player.account.AccountOperations;
import com.gogomaya.server.integration.player.listener.PlayerListenerOperations;
import com.gogomaya.server.integration.player.profile.ProfileOperations;
import com.gogomaya.server.integration.player.session.SessionOperations;
import com.gogomaya.web.management.ManagementWebMapping;
import com.gogomaya.web.player.PlayerWebMapping;

public class IntegrationPlayerOperations extends AbstractPlayerOperations {

    final private String baseUrl;
    final private RestTemplate restTemplate;

    public IntegrationPlayerOperations(final String baseUrl,
            final RestTemplate restTemplate,
            final PlayerListenerOperations listenerOperations,
            final ProfileOperations playerProfileOperations,
            final SessionOperations playerSessionOperations,
            final AccountOperations accountOperations) {
        super(listenerOperations, playerProfileOperations, playerSessionOperations, accountOperations);
        this.baseUrl = checkNotNull(baseUrl);
        this.restTemplate = checkNotNull(restTemplate);
    }

    @Override
    public Player createPlayer(PlayerRegistrationRequest registrationRequest) {
        // Step 0. Sanity check
        checkNotNull(registrationRequest);
        // Step 1. Performing actual player creation
        PlayerIdentity playerIdentity = restTemplate.postForObject(baseUrl + ManagementWebMapping.MANAGEMENT_PREFIX + ManagementWebMapping.PLAYER_REGISTRATION_SIGN_IN,
                registrationRequest, PlayerIdentity.class);
        checkNotNull(playerIdentity);
        // Step 2. Generating Player from created request
        Player player = super.create(playerIdentity, registrationRequest.getPlayerCredential());
        // Step 3. Returning player session result
        return player;
    }

    @Override
    public Player login(PlayerLoginRequest credential) {
        // Step 0. Sanity check
        checkNotNull(credential);
        // Step 1. Performing actual player login
        PlayerIdentity playerIdentity = restTemplate.postForObject(baseUrl + ManagementWebMapping.MANAGEMENT_PREFIX + ManagementWebMapping.PLAYER_REGISTRATION_LOGIN,
                credential, PlayerIdentity.class);
        checkNotNull(playerIdentity);
        // Step 2. Generating Player from credentials
        Player player = super.create(playerIdentity, credential.getPlayerCredential());
        // Step 3. Returning player session result
        return player;
    }

}
