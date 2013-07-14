package com.gogomaya.server.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.server.integration.game.construction.GameConstructionOperations;
import com.gogomaya.server.integration.player.listener.PlayerListenerOperations;
import com.gogomaya.server.integration.player.profile.PlayerProfileOperations;
import com.gogomaya.server.player.security.PlayerCredential;
import com.gogomaya.server.player.security.PlayerIdentity;
import com.gogomaya.server.player.security.PlayerSession;
import com.gogomaya.server.player.wallet.PlayerWallet;
import com.gogomaya.server.player.web.RegistrationRequest;
import com.gogomaya.server.web.mapping.PaymentWebMapping;
import com.gogomaya.server.web.mapping.PlayerWebMapping;

public class IntegrationPlayerOperations extends AbstractPlayerOperations {

    final private String baseUrl;
    final private RestTemplate restTemplate;

    public IntegrationPlayerOperations(final String baseUrl, final RestTemplate restTemplate, final PlayerListenerOperations listenerOperations, PlayerProfileOperations playerProfileOperations, GameConstructionOperations<?> ... factories) {
        super(listenerOperations, playerProfileOperations, factories);
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

    public PlayerSession startSession(PlayerIdentity player) {
        // Step 1. Creating Header
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(player.getPlayerId()));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        HttpEntity<Void> requestEntity = new HttpEntity<Void>(null, header);
        // Step 3. Rest template generation
        return restTemplate.exchange(baseUrl + PlayerWebMapping.PLAYER_PREFIX + PlayerWebMapping.PLAYER_SESSION_LOGIN, HttpMethod.POST, requestEntity, PlayerSession.class).getBody();
    }

    @Override
    public PlayerWallet wallet(Player player, long playerWalletId) {
        // Step 1. Creating Header
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(player.getPlayerId()));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        HttpEntity<Void> requestEntity = new HttpEntity<Void>(null, header);
        // Step 3. Rest template generation
        return restTemplate.exchange(baseUrl + PaymentWebMapping.WALLET_PREFIX + PaymentWebMapping.WALLET_PLAYER, HttpMethod.GET, requestEntity, PlayerWallet.class, playerWalletId).getBody();
    }

}
