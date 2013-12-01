package com.clemble.casino.android;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;

import com.clemble.casino.android.player.AndroidPlayerRegistrationService;
import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.ClembleCasinoRegistrationOperations;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.SocialConnectionData;
import com.clemble.casino.player.client.ClembleConsumerDetails;
import com.clemble.casino.player.security.PlayerCredential;
import com.clemble.casino.player.security.PlayerToken;
import com.clemble.casino.player.service.PlayerRegistrationService;
import com.clemble.casino.player.web.PlayerLoginRequest;
import com.clemble.casino.player.web.PlayerRegistrationRequest;
import com.clemble.casino.player.web.PlayerSocialRegistrationRequest;
import com.clemble.casino.utils.ClembleConsumerDetailUtils;

public class AndroidCasinoRegistrationTemplate implements ClembleCasinoRegistrationOperations {

    final private String managementUrl;
    final private PlayerRegistrationService playerRegistrationService;

    public AndroidCasinoRegistrationTemplate(String managementUrl) {
        this.managementUrl = managementUrl;
        this.playerRegistrationService = new AndroidPlayerRegistrationService(managementUrl);
    }

    @Override
    public ClembleCasinoOperations login(PlayerCredential playerCredential) {
        // Step 1. Generating consumer details
        ClembleConsumerDetails consumerDetails = ClembleConsumerDetailUtils.generateDetails();
        // Step 2. Generating login request
        PlayerLoginRequest loginRequest = new PlayerLoginRequest(consumerDetails, playerCredential);
        // Step 3. Constructing casino operations
        return casinoTemplate(playerRegistrationService.login(loginRequest), consumerDetails);
    }

    @Override
    public ClembleCasinoOperations createPlayer(PlayerCredential playerCredential, PlayerProfile playerProfile) {
        // Step 1. Generating consumer details
        ClembleConsumerDetails consumerDetails = ClembleConsumerDetailUtils.generateDetails();
        // Step 2. Generating login request
        PlayerRegistrationRequest loginRequest = new PlayerRegistrationRequest(playerProfile, playerCredential, consumerDetails);
        // Step 3. Constructing casino operations
        return casinoTemplate(playerRegistrationService.createPlayer(loginRequest), consumerDetails);
    }

    @Override
    public ClembleCasinoOperations createSocialPlayer(PlayerCredential playerCredential, SocialConnectionData socialConnectionData) {
        // Step 1. Generating consumer details
        ClembleConsumerDetails consumerDetails = ClembleConsumerDetailUtils.generateDetails();
        // Step 2. Generating login request
        PlayerSocialRegistrationRequest socialRegistrationRequest = new PlayerSocialRegistrationRequest(consumerDetails, playerCredential, socialConnectionData);
        // Step 3. Constructing casino operations
        return casinoTemplate(playerRegistrationService.createSocialPlayer(socialRegistrationRequest), consumerDetails);
    }

    private ClembleCasinoTemplate casinoTemplate(PlayerToken token, ClembleConsumerDetails consumerDetails) {
        String consumerKey = token.getConsumerKey();
        String consumerSecret = new String(Base64.encodeBase64(consumerDetails.getSignatureSecret().getPrivateKey().getEncoded()), Charset.forName("UTF-8"));
        String accessToken = token.getValue();
        String accessTokenSecret = String.valueOf(token.getSecretKey().getEncoded());
        String player = token.getPlayer();
        try {
            return new ClembleCasinoTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret, player, managementUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
