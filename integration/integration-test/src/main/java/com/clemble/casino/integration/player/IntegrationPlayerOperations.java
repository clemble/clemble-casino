package com.clemble.casino.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.web.client.RestTemplate;

import com.clemble.casino.integration.player.account.AccountOperations;
import com.clemble.casino.integration.player.listener.PlayerListenerOperations;
import com.clemble.casino.integration.player.profile.ProfileOperations;
import com.clemble.casino.player.security.PlayerToken;
import com.clemble.casino.player.service.PlayerSessionService;
import com.clemble.casino.player.web.PlayerLoginRequest;
import com.clemble.casino.player.web.PlayerRegistrationRequest;
import com.clemble.casino.web.management.ManagementWebMapping;

public class IntegrationPlayerOperations extends AbstractPlayerOperations {

    final private String baseUrl;
    final private RestTemplate restTemplate;

    public IntegrationPlayerOperations(final String baseUrl,
            final RestTemplate restTemplate,
            final PlayerListenerOperations listenerOperations,
            final ProfileOperations playerProfileOperations,
            final PlayerSessionService playerSessionOperations,
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
        PlayerToken playerIdentity = restTemplate.postForObject(baseUrl + ManagementWebMapping.MANAGEMENT_PLAYER_REGISTRATION,
                registrationRequest, PlayerToken.class);
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
        PlayerToken playerIdentity = restTemplate.postForObject(baseUrl + ManagementWebMapping.MANAGEMENT_PLAYER_LOGIN,
                credential, PlayerToken.class);
        checkNotNull(playerIdentity);
        // Step 2. Generating Player from credentials
        Player player = super.create(playerIdentity, credential.getPlayerCredential());
        // Step 3. Returning player session result
        return player;
    }

}
