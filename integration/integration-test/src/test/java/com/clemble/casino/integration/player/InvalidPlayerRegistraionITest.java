package com.clemble.casino.integration.player;

import java.util.Date;

import com.clemble.casino.integration.ClembleIntegrationTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.test.util.ClembleCasinoExceptionMatcherFactory;
import com.clemble.casino.player.PlayerGender;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.registration.PlayerCredential;

@RunWith(SpringJUnit4ClassRunner.class)
@ClembleIntegrationTest
public class InvalidPlayerRegistraionITest {

    @Autowired
    public PlayerScenarios playerScenarios;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void registerInvalidProfile() {
        PlayerProfile playerProfile = new PlayerProfile().
            setBirthDate(new DateTime(0)).
            setFirstName(RandomStringUtils.randomAlphabetic(10)).
            setGender(PlayerGender.M).
            setLastName(RandomStringUtils.randomAlphabetic(10)).
            setNickName(RandomStringUtils.randomAlphabetic(10)).
            setPlayer(RandomStringUtils.random(5)).
            addSocialConnection(new ConnectionKey("facebook", "2132432"));
        PlayerCredential playerCredential = new PlayerCredential(RandomStringUtils.randomAlphabetic(5) + "@gmail.com", RandomStringUtils.randomAlphabetic(7));

        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.ProfileSocialMustBeEmpty));

        playerScenarios.createPlayer(playerCredential, playerProfile);
    }

}
