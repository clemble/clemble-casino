package com.clemble.casino.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.oauth.common.signature.RSAKeySecret;

import com.clemble.casino.android.ClembleCasinoTemplate;
import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.player.NativePlayerProfile;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.client.ClembleConsumerDetails;
import com.clemble.casino.player.client.ClientDetails;
import com.clemble.casino.player.security.PlayerCredential;
import com.clemble.casino.player.security.PlayerToken;
import com.clemble.casino.player.service.PlayerRegistrationService;
import com.clemble.casino.player.web.PlayerLoginRequest;
import com.clemble.casino.player.web.PlayerRegistrationRequest;
import com.clemble.test.random.ObjectGenerator;

public class IntegrationPlayerOperations implements PlayerOperations {

    final private String baseUrl;
    final private PlayerRegistrationService registrationService;

    public IntegrationPlayerOperations(String baseUrl, PlayerRegistrationService registrationService) {
        this.baseUrl = checkNotNull(baseUrl);
        this.registrationService = checkNotNull(registrationService);
    }

    @Override
    public ClembleCasinoOperations createPlayer(PlayerRegistrationRequest registrationRequest) {
        // Step 0. Sanity check
        checkNotNull(registrationRequest);
        // Step 1. Performing actual player creation
        PlayerToken token = registrationService.createPlayer(registrationRequest);
        checkNotNull(token);
        // Step 2. Generating Player from created request
        return create(registrationRequest, token);
    }

    @Override
    public ClembleCasinoOperations login(PlayerLoginRequest credential) {
        // Step 0. Sanity check
        checkNotNull(credential);
        // Step 1. Performing actual player login
        PlayerToken token = registrationService.login(credential);
        checkNotNull(token);
        // Step 2. Generating Player from credentials
        return create(credential, token);
    }

    @Override
    final public ClembleCasinoOperations createPlayer() {
        return createPlayer(new NativePlayerProfile()
            .setFirstName(RandomStringUtils.randomAlphabetic(10))
            .setLastName(RandomStringUtils.randomAlphabetic(10))
            .setNickName(RandomStringUtils.randomAlphabetic(10)));
    }

    @Override
    final public ClembleCasinoOperations createPlayer(PlayerProfile playerProfile) {
        // Step 0. Sanity check
        checkNotNull(playerProfile);
        // Step 1. Creating RegistrationRequest for processing
        PlayerCredential playerCredential = new PlayerCredential().setEmail(RandomStringUtils.randomAlphabetic(30) + "@gmail.com").setPassword(UUID.randomUUID().toString());
        ClembleConsumerDetails consumerDetails = new ClembleConsumerDetails(UUID.randomUUID().toString(), "IT", ObjectGenerator.generate(RSAKeySecret.class), null, new ClientDetails("IT"));
        PlayerRegistrationRequest registrationRequest = new PlayerRegistrationRequest(playerProfile, playerCredential, consumerDetails);
        // Step 2. Forwarding to appropriate method for processing
        return createPlayer(registrationRequest);
    }

    final public ClembleCasinoOperations create(PlayerLoginRequest registration, PlayerToken token) {
        String consumerKey = token.getConsumerKey();
        String consumerSecret = Base64.encodeBase64String(registration.getConsumerDetails().getSignatureSecret().getPrivateKey().getEncoded());
        String accessToken = token.getValue();
        String accessTokenSecret = String.valueOf(token.getSecretKey().getEncoded());
        String player = token.getPlayer();
        try {
            return new ClembleCasinoTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret, player, baseUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
