package com.clemble.casino.integration.game.construction;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.RandomStringUtils;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.ClembleCasinoRegistrationOperations;
import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.event.Event;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.SocialAccessGrant;
import com.clemble.casino.player.SocialConnectionData;
import com.clemble.casino.player.security.PlayerCredential;
import com.clemble.casino.player.web.PlayerRegistrationRequest;

public class SimplePlayerScenarios implements PlayerScenarios {

    final private ClembleCasinoRegistrationOperations registrationOperations;

    public SimplePlayerScenarios(ClembleCasinoRegistrationOperations registrationOperations) {
        this.registrationOperations = checkNotNull(registrationOperations);
    }

    @Override
    public ClembleCasinoOperations createPlayer() {
        PlayerCredential credential = new PlayerCredential().setEmail(RandomStringUtils.randomAlphabetic(10) + "@gmail.com").setPassword(
                RandomStringUtils.randomAlphanumeric(10));
        PlayerProfile playerProfile = new PlayerProfile().setFirstName(RandomStringUtils.randomAlphabetic(10))
                .setLastName(RandomStringUtils.randomAlphabetic(10)).setNickName(RandomStringUtils.randomAlphabetic(10));
        return createPlayer(credential, playerProfile);
    }

    @Override
    public ClembleCasinoOperations createPlayer(PlayerProfile playerProfile) {
        PlayerCredential credential = new PlayerCredential()
            .setEmail(RandomStringUtils.randomAlphabetic(10) + "@gmail.com")
            .setPassword( RandomStringUtils.randomAlphanumeric(10));
        return createPlayer(credential, playerProfile);
    }

    @Override
    public ClembleCasinoOperations createPlayer(SocialConnectionData socialConnectionData) {
        PlayerCredential credential = new PlayerCredential().setEmail(RandomStringUtils.randomAlphabetic(10) + "@gmail.com").setPassword(
                RandomStringUtils.randomAlphanumeric(10));
        return createSocialPlayer(credential, socialConnectionData);
    }

    @Override
    public ClembleCasinoOperations createPlayer(SocialAccessGrant socialConnectionData) {
        // Step 1. Generating random PlayerCredentials
        PlayerCredential credential = new PlayerCredential().setEmail(RandomStringUtils.randomAlphabetic(10) + "@gmail.com").setPassword(
                RandomStringUtils.randomAlphanumeric(10));
        // Step 2. Create SocialPlayer profile
        return createSocialPlayer(credential, socialConnectionData);
    }

    @Override
    public ClembleCasinoOperations createPlayer(PlayerRegistrationRequest playerRegistrationRequest) {
        return createPlayer(playerRegistrationRequest.getPlayerCredential(), playerRegistrationRequest.getPlayerProfile());
    }

    @Override
    public ClembleCasinoOperations login(PlayerCredential playerCredentials) {
        return initialize(registrationOperations.login(playerCredentials));
    }

    @Override
    public ClembleCasinoOperations createPlayer(PlayerCredential playerCredential, PlayerProfile playerProfile) {
        return initialize(registrationOperations.createPlayer(playerCredential, playerProfile));
    }

    @Override
    public ClembleCasinoOperations createSocialPlayer(PlayerCredential playerCredential, SocialConnectionData socialConnectionData) {
        return initialize(registrationOperations.createSocialPlayer(playerCredential, socialConnectionData));
    }

    @Override
    public ClembleCasinoOperations createSocialPlayer(PlayerCredential playerCredential, SocialAccessGrant accessGrant) {
        return initialize(registrationOperations.createSocialPlayer(playerCredential, accessGrant));
    }

    private ClembleCasinoOperations initialize(final ClembleCasinoOperations player) {
        player.listenerOperations().subscribe(new EventListener<Event>() {
            final private AtomicInteger messageNum = new AtomicInteger();

            @Override
            public void onEvent(Event event) {
                System.out.println("event >> " + messageNum.incrementAndGet() + " >> " + player.getPlayer() + " >> " + event);
            }
        });
        while (!player.listenerOperations().isAlive()) {
            try {
                Thread.sleep(100);
            } catch (Throwable throwable) {
            }
        }
        return player;
    }
}