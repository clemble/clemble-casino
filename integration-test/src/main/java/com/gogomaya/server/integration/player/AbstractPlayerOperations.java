package com.gogomaya.server.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import com.gogomaya.server.player.PlayerProfile;
import com.gogomaya.server.player.security.PlayerCredential;
import com.gogomaya.server.player.web.RegistrationRequest;

abstract public class AbstractPlayerOperations implements PlayerOperations {

    @Override
    final public Player createPlayer() {
        return createPlayer(new PlayerProfile()
            .setFirstName(RandomStringUtils.random(10))
            .setLastName(RandomStringUtils.random(10))
            .setNickName(RandomStringUtils.random(10)));
    }

    @Override
    final public Player createPlayer(PlayerProfile playerProfile) {
        // Step 0. Sanity check
        checkNotNull(playerProfile);
        // Step 1. Creating RegistrationRequest for processing
        PlayerCredential playerCredential = new PlayerCredential().setEmail(UUID.randomUUID().toString() + "@gmail.com").setPassword(
                UUID.randomUUID().toString());
        RegistrationRequest registrationRequest = new RegistrationRequest().setPlayerProfile(playerProfile).setPlayerCredential(playerCredential);
        // Step 2. Forwarding to appropriate method for processing
        return createPlayer(registrationRequest);
    }

}
