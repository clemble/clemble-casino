package com.clemble.casino.integration.game.construction;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.concurrent.atomic.AtomicInteger;

import com.clemble.casino.integration.event.EventAccumulator;
import com.clemble.casino.integration.utils.AsyncUtils;
import com.clemble.casino.payment.bonus.RegistrationBonusPaymentSource;
import com.clemble.casino.registration.PlayerLoginRequest;
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
        return register(credential, playerProfile);
    }

    @Override
    public ClembleCasinoOperations createPlayer(PlayerProfile playerProfile) {
        PlayerCredential credential = new PlayerCredential(
            RandomStringUtils.randomAlphabetic(10) + "@gmail.com",
            RandomStringUtils.randomAlphanumeric(10));
        return register(credential, playerProfile);
    }

    @Override
    public ClembleCasinoOperations createPlayer(SocialConnectionData socialConnectionData) {
        PlayerCredential credential = new PlayerCredential(
            RandomStringUtils.randomAlphabetic(10) + "@gmail.com",
            RandomStringUtils.randomAlphanumeric(10));
        return register(credential, socialConnectionData);
    }

    @Override
    public ClembleCasinoOperations createPlayer(SocialAccessGrant socialConnectionData) {
        // Step 1. Generating random PlayerCredentials
        PlayerCredential credential = new PlayerCredential(
            RandomStringUtils.randomAlphabetic(10) + "@gmail.com",
            RandomStringUtils.randomAlphanumeric(10));
        // Step 2. Create SocialPlayer profile
        return register(credential, socialConnectionData);
    }

    @Override
    public ClembleCasinoOperations createPlayer(PlayerRegistrationRequest playerRegistrationRequest) {
        return register(playerRegistrationRequest.toCredentials(), playerRegistrationRequest.toProfileWithPlayer(null));
    }

    @Override
    public ClembleCasinoOperations login(PlayerLoginRequest loginRequest) {
        return initialize(registrationOperations.login(loginRequest));
    }

    @Override
    public ClembleCasinoOperations register(PlayerCredential playerCredential, PlayerProfile playerProfile) {
        return initialize(registrationOperations.register(playerCredential, playerProfile));
    }

    @Override
    public ClembleCasinoOperations register(PlayerCredential playerCredential, SocialConnectionData socialConnectionData) {
        return initialize(registrationOperations.register(playerCredential, socialConnectionData));
    }

    @Override
    public ClembleCasinoOperations register(PlayerCredential playerCredential, SocialAccessGrant accessGrant) {
        return initialize(registrationOperations.register(playerCredential, accessGrant));
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
        AsyncUtils.check((i) -> player.listenerOperations().isAlive());
        // Step 2. Checking registration && daily bonus received
        final String registrationTransaction = RegistrationBonusPaymentSource.INSTANCE.toTransactionKey(player.getPlayer());
        AsyncUtils.check((i) -> player.paymentOperations().getTransaction(registrationTransaction) != null);
        AsyncUtils.check((i) -> player.paymentOperations().myTransactionsBySource("dailybonus").size() > 0);
        // Step 3. Getting PaymentTransaction
        AsyncUtils.checkNotNull((i) -> player.profileOperations().myProfile());
        // Step 4. Getting PlayerConnection
        AsyncUtils.checkNotNull((i) -> player.connectionOperations().myConnections());
        return player;
    }

}
