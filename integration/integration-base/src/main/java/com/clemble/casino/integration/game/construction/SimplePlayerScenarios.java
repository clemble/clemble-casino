package com.clemble.casino.integration.game.construction;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import org.apache.commons.lang3.RandomStringUtils;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.ClembleCasinoRegistrationOperations;
import com.clemble.casino.player.NativePlayerProfile;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.SocialConnectionData;
import com.clemble.casino.player.security.PlayerCredential;

public class SimplePlayerScenarios implements PlayerScenarios {

    final private ClembleCasinoRegistrationOperations registrationOperations;

    public SimplePlayerScenarios(ClembleCasinoRegistrationOperations registrationOperations) {
        this.registrationOperations = checkNotNull(registrationOperations);
    }

    @Override
    public ClembleCasinoOperations createPlayer() {
        return registrationOperations.createPlayer(
                new PlayerCredential().setEmail(RandomStringUtils.randomAlphabetic(10) + "@gmail.com").setPassword(RandomStringUtils.randomAlphanumeric(5)),
                new NativePlayerProfile().setFirstName(RandomStringUtils.randomAlphabetic(10)).setLastName(RandomStringUtils.randomAlphabetic(10)).setNickName(RandomStringUtils.randomAlphabetic(10)));

    }

    @Override
    public ClembleCasinoOperations createPlayer(PlayerProfile playerProfile) {
        return registrationOperations.createPlayer(
                new PlayerCredential().setEmail(RandomStringUtils.randomAlphabetic(10) + "@gmail.com").setPassword(RandomStringUtils.randomAlphanumeric(5)),
                playerProfile);
    }

    @Override
    public ClembleCasinoOperations createPlayer(SocialConnectionData socialConnectionData) {
        return registrationOperations.createSocialPlayer(
                new PlayerCredential().setEmail(RandomStringUtils.randomAlphabetic(10) + "@gmail.com").setPassword(RandomStringUtils.randomAlphanumeric(5)),
                socialConnectionData);
    }

}
