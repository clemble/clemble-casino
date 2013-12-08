package com.clemble.casino.integration.game.construction;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.RandomStringUtils;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.ClembleCasinoRegistrationOperations;
import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.event.Event;
import com.clemble.casino.player.NativePlayerProfile;
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
        ClembleCasinoOperations player = registrationOperations.createPlayer(
                new PlayerCredential().setEmail(RandomStringUtils.randomAlphabetic(10) + "@gmail.com").setPassword(RandomStringUtils.randomAlphanumeric(10)),
                new NativePlayerProfile().setFirstName(RandomStringUtils.randomAlphabetic(10)).setLastName(RandomStringUtils.randomAlphabetic(10))
                        .setNickName(RandomStringUtils.randomAlphabetic(10)));

        return initialize(player);

    }

    @Override
    public ClembleCasinoOperations createPlayer(PlayerProfile playerProfile) {
        ClembleCasinoOperations player = registrationOperations.createPlayer(
                new PlayerCredential().setEmail(RandomStringUtils.randomAlphabetic(10) + "@gmail.com").setPassword(RandomStringUtils.randomAlphanumeric(10)),
                playerProfile);

        return initialize(player);
    }

    @Override
    public ClembleCasinoOperations createPlayer(SocialConnectionData socialConnectionData) {
        ClembleCasinoOperations player = registrationOperations.createSocialPlayer(
                new PlayerCredential().setEmail(RandomStringUtils.randomAlphabetic(10) + "@gmail.com").setPassword(RandomStringUtils.randomAlphanumeric(10)),
                socialConnectionData);

        return initialize(player);
    }

    @Override
    public ClembleCasinoOperations createPlayer(PlayerRegistrationRequest playerRegistrationRequest) {
        ClembleCasinoOperations player = registrationOperations.createPlayer(playerRegistrationRequest.getPlayerCredential(),
                playerRegistrationRequest.getPlayerProfile());

        return initialize(player);
    }

    private ClembleCasinoOperations initialize(final ClembleCasinoOperations player) {
        player.listenerOperations().subscribe(new EventListener() {
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

    @Override
    public ClembleCasinoOperations login(PlayerCredential playerCredentials) {
        return registrationOperations.login(playerCredentials);
    }

    @Override
    public ClembleCasinoOperations createPlayer(PlayerCredential playerCredential, PlayerProfile playerProfile) {
        return registrationOperations.createPlayer(playerCredential, playerProfile);
    }

    @Override
    public ClembleCasinoOperations createSocialPlayer(PlayerCredential playerCredential, SocialConnectionData socialConnectionData) {
        return registrationOperations.createSocialPlayer(playerCredential, socialConnectionData);
    }

    @Override
    public ClembleCasinoOperations createSocialPlayer(PlayerCredential playerCredential, SocialAccessGrant accessGrant) {
        return registrationOperations.createSocialPlayer(playerCredential, accessGrant);
    }

}
