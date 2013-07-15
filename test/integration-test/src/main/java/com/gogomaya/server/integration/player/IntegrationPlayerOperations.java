package com.gogomaya.server.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.server.integration.game.construction.GameConstructionOperations;
import com.gogomaya.server.integration.player.listener.PlayerListenerOperations;
import com.gogomaya.server.integration.player.profile.ProfileOperations;
import com.gogomaya.server.integration.player.session.SessionOperations;
import com.gogomaya.server.player.security.PlayerCredential;
import com.gogomaya.server.player.security.PlayerIdentity;
import com.gogomaya.server.player.wallet.PlayerWallet;
import com.gogomaya.server.player.web.RegistrationRequest;
import com.gogomaya.server.web.mapping.PaymentWebMapping;
import com.gogomaya.server.web.mapping.PlayerWebMapping;

public class IntegrationPlayerOperations extends AbstractPlayerOperations {

    final private String baseUrl;
    final private RestTemplate restTemplate;

    public IntegrationPlayerOperations(final String baseUrl, final RestTemplate restTemplate, final PlayerListenerOperations listenerOperations, ProfileOperations playerProfileOperations, SessionOperations playerSessionOperations, GameConstructionOperations<?> ... factories) {
        super(listenerOperations, playerProfileOperations, playerSessionOperations, factories);
        this.baseUrl = checkNotNull(baseUrl);
        this.restTemplate = checkNotNull(restTemplate);
    }

    @Override
    public Player createPlayer(RegistrationRequest registrationRequest) {
        // Step 0. Sanity check
        checkNotNull(registrationRequest);
        // Step 1. Performing actual player creation
        PlayerIdentity playerIdentity = restTemplate.postForObject(baseUrl + PlayerWebMapping.PLAYER_PREFIX + PlayerWebMapping.PLAYER_REGISTRATION_SIGN_IN, registrationRequest, PlayerIdentity.class);
        checkNotNull(playerIdentity);
        // Step 2. Generating Player from created request
        Player player = super.create(playerIdentity, registrationRequest.getPlayerCredential());
        // Step 3. Returning player session result
        return player;
    }

    @Override
    public Player login(PlayerCredential credential) {
        // Step 0. Sanity check
        checkNotNull(credential);
        // Step 1. Performing actual player login
        PlayerIdentity playerIdentity = restTemplate.postForObject(baseUrl + PlayerWebMapping.PLAYER_PREFIX + PlayerWebMapping.PLAYER_REGISTRATION_LOGIN, credential, PlayerIdentity.class);
        checkNotNull(playerIdentity);
        // Step 2. Generating Player from credentials
        Player player = super.create(playerIdentity, credential);
        // Step 3. Returning player session result
        return player;
    }

    @Override
    public PlayerWallet wallet(Player player, long playerWalletId) {
        // Step 1. Generating signed request
        HttpEntity<Void> requestEntity = player.<Void>sign(null);
        // Step 3. Rest template generation
        return restTemplate.exchange(baseUrl + PaymentWebMapping.WALLET_PREFIX + PaymentWebMapping.WALLET_PLAYER, HttpMethod.GET, requestEntity, PlayerWallet.class, playerWalletId).getBody();
    }

}
