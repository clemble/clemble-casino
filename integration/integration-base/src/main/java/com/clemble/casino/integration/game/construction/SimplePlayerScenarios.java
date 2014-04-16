package com.clemble.casino.integration.game.construction;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.concurrent.atomic.AtomicInteger;

import com.clemble.casino.client.payment.PaymentTransactionEventSelector;
import com.clemble.casino.integration.event.EventAccumulator;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PaymentTransactionKey;
import com.clemble.test.concurrent.AsyncCompletionUtils;
import com.clemble.test.concurrent.Check;
import com.clemble.test.concurrent.Get;
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
import org.junit.Assert;

public class SimplePlayerScenarios implements PlayerScenarios {

    final private ClembleCasinoRegistrationOperations registrationOperations;

    public SimplePlayerScenarios(ClembleCasinoRegistrationOperations registrationOperations) {
        this.registrationOperations = checkNotNull(registrationOperations);
    }

    @Override
    public ClembleCasinoOperations createPlayer() {
        PlayerCredential credential = new PlayerCredential()
                .setEmail(RandomStringUtils.randomAlphabetic(10) + "@gmail.com")
                .setPassword(RandomStringUtils.randomAlphanumeric(10));
        PlayerProfile playerProfile = new PlayerProfile()
                .setFirstName(RandomStringUtils.randomAlphabetic(10))
                .setLastName(RandomStringUtils.randomAlphabetic(10))
                .setNickName(RandomStringUtils.randomAlphabetic(10));
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
        PlayerCredential credential = new PlayerCredential()
                .setEmail(RandomStringUtils.randomAlphabetic(10) + "@gmail.com")
                .setPassword(RandomStringUtils.randomAlphanumeric(10));
        return createSocialPlayer(credential, socialConnectionData);
    }

    @Override
    public ClembleCasinoOperations createPlayer(SocialAccessGrant socialConnectionData) {
        // Step 1. Generating random PlayerCredentials
        PlayerCredential credential = new PlayerCredential()
                .setEmail(RandomStringUtils.randomAlphabetic(10) + "@gmail.com")
                .setPassword(RandomStringUtils.randomAlphanumeric(10));
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
        }, 30_000);
        // Step 2. Checking registration passed
        PaymentTransactionKey registrationTransaction = new PaymentTransactionKey("registration", player.getPlayer());
        if (player.paymentOperations().getPaymentTransaction(registrationTransaction) == null)
            Assert.assertNotNull("Missing transaction : " + registrationTransaction, eventAccumulator.waitFor(new PaymentTransactionEventSelector(registrationTransaction), 30_000));
        return player;
    }
}
