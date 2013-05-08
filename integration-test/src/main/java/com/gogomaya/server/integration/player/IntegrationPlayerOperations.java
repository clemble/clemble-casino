package com.gogomaya.server.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.server.player.security.PlayerCredential;
import com.gogomaya.server.player.security.PlayerIdentity;
import com.gogomaya.server.player.wallet.PlayerWallet;
import com.gogomaya.server.player.web.RegistrationRequest;

public class IntegrationPlayerOperations extends AbstractPlayerOperations {

    final private static String CREATE_URL = "/spi/registration/signin";
    final private static String LOGIN_URL = "/spi/registration/login";
    final private static String WALLET_URL = "/spi/wallet/";

    final private String baseUrl;
    final private RestTemplate restTemplate;

    public IntegrationPlayerOperations(final String baseUrl, final RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public Player createPlayer(RegistrationRequest registrationRequest) {
        // Step 0. Sanity check
        checkNotNull(registrationRequest);
        // Step 1. Performing actual player creation
        PlayerIdentity playerIdentity = restTemplate.postForObject(baseUrl + CREATE_URL, registrationRequest, PlayerIdentity.class);
        checkNotNull(playerIdentity);
        // Step 2. Generating Player from created request
        return new Player(this).setPlayerId(playerIdentity.getPlayerId()).setIdentity(playerIdentity).setProfile(registrationRequest.getPlayerProfile())
                .setCredential(registrationRequest.getPlayerCredential());
    }

    @Override
    public Player login(PlayerCredential credential) {
        // Step 0. Sanity check
        checkNotNull(credential);
        // Step 1. Performing actual player login
        PlayerIdentity playerIdentity = restTemplate.postForObject(baseUrl + LOGIN_URL, credential, PlayerIdentity.class);
        checkNotNull(playerIdentity);
        // Step 2. Generating Player from credentials
        return new Player(this).setPlayerId(playerIdentity.getPlayerId()).setCredential(credential).setIdentity(playerIdentity);
    }

    @Override
    public PlayerWallet wallet(Player player, long playerWalletId) {
        // Step 1.
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(player.getPlayerId()));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        HttpEntity<Void> requestEntity = new HttpEntity<Void>(null, header);
        // Step 3. Rest template generation
        return restTemplate.exchange(baseUrl + WALLET_URL + playerWalletId, HttpMethod.GET, requestEntity, PlayerWallet.class).getBody();
    }

}
