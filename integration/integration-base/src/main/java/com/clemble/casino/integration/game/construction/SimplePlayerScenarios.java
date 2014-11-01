package com.clemble.casino.integration.game.construction;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.clemble.casino.integration.event.EventAccumulator;
import com.clemble.casino.money.MoneySource;
import com.clemble.casino.payment.bonus.PaymentBonusSource;
import com.clemble.casino.player.PlayerConnections;
import com.clemble.test.concurrent.AsyncCompletionUtils;
import com.clemble.test.concurrent.Check;
import com.clemble.test.concurrent.Get;
import org.apache.commons.lang3.RandomStringUtils;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.ClembleCasinoRegistrationOperations;
import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.event.Event;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.social.SocialAccessGrant;
import com.clemble.casino.social.SocialConnectionData;
import com.clemble.casino.registration.PlayerCredential;
import com.clemble.casino.registration.PlayerRegistrationRequest;
import org.springframework.social.connect.ConnectionKey;

public class SimplePlayerScenarios implements PlayerScenarios {

    final private ClembleCasinoRegistrationOperations registrationOperations;

    public SimplePlayerScenarios(ClembleCasinoRegistrationOperations registrationOperations) {
        this.registrationOperations = checkNotNull(registrationOperations);
    }

    @Override
    public ClembleCasinoOperations createPlayer() {
        PlayerCredential credential = new PlayerCredential(
            RandomStringUtils.randomAlphabetic(10) + "@gmail.com",
            RandomStringUtils.randomAlphanumeric(10));
        PlayerProfile playerProfile = new PlayerProfile()
                .setFirstName(RandomStringUtils.randomAlphabetic(10))
                .setLastName(RandomStringUtils.randomAlphabetic(10))
                .setNickName(RandomStringUtils.randomAlphabetic(10));
        return createPlayer(credential, playerProfile);
    }

    @Override
    public ClembleCasinoOperations createPlayer(PlayerProfile playerProfile) {
        PlayerCredential credential = new PlayerCredential(
            RandomStringUtils.randomAlphabetic(10) + "@gmail.com",
            RandomStringUtils.randomAlphanumeric(10));
        return createPlayer(credential, playerProfile);
    }

    @Override
    public ClembleCasinoOperations createPlayer(SocialConnectionData socialConnectionData) {
        PlayerCredential credential = new PlayerCredential(
            RandomStringUtils.randomAlphabetic(10) + "@gmail.com",
            RandomStringUtils.randomAlphanumeric(10));
        return createSocialPlayer(credential, socialConnectionData);
    }

    @Override
    public ClembleCasinoOperations createPlayer(SocialAccessGrant socialConnectionData) {
        // Step 1. Generating random PlayerCredentials
        PlayerCredential credential = new PlayerCredential(
            RandomStringUtils.randomAlphabetic(10) + "@gmail.com",
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
        EventAccumulator<Event> eventAccumulator = new EventAccumulator<>();
        player.listenerOperations().subscribe(eventAccumulator);
        player.listenerOperations().subscribe(new EventListener<Event>() {
            final private AtomicInteger messageNum = new AtomicInteger();

            @Override
            public void onEvent(Event event) {
                System.out.println("event >> " + messageNum.incrementAndGet() + " >> " + player.getPlayer() + " >> " + event);
            }
        });
        // Step 1. Checking listener was able to connect
        AsyncCompletionUtils.check(new Check() {
            @Override
            public boolean check() {
                return player.listenerOperations().isAlive();
            }
        }, 5_000);
        // Step 2. Checking registration && daily bonus received
        final String registrationTransaction = player.getPlayer() + MoneySource.registration;
        AsyncCompletionUtils.check(new Check() {
            @Override
            public boolean check() {
                return player.paymentOperations().getTransaction(registrationTransaction) != null;
            }
        }, 15_000, 1_000);
        AsyncCompletionUtils.check(new Check() {
            @Override
            public boolean check() {
                return player.paymentOperations().myTransactions(PaymentBonusSource.dailybonus).size() > 0;
            }
        }, 15_000, 1_000);
        // Step 3. Getting PaymentTransaction
        AsyncCompletionUtils.get(new Get<PlayerProfile>() {
            @Override
            public PlayerProfile get() {
                return player.profileOperations().myProfile();
            }
        }, 5_000);
        // Step 4. Getting PlayerConnection
        AsyncCompletionUtils.get(new Get<PlayerConnections>() {
            @Override
            public PlayerConnections get() {
                return player.connectionOperations().myConnections();
            }
        }, 15_000, 1_000);
        return player;
    }
}
