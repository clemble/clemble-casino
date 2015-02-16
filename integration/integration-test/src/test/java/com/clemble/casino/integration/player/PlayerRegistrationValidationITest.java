package com.clemble.casino.integration.player;

import com.clemble.casino.integration.ClembleIntegrationTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.test.util.ClembleCasinoExceptionMatcherFactory;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.registration.PlayerCredential;
import com.clemble.test.random.ObjectGenerator;

@RunWith(SpringJUnit4ClassRunner.class)
@ClembleIntegrationTest
public class PlayerRegistrationValidationITest {

    @Autowired
    public PlayerScenarios playerScenarios;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testInvalidEmail() {
        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.EmailInvalid));
        playerScenarios.register(new PlayerCredential(ObjectGenerator.generate(String.class) + "test_gmail.com", "aVsdsvdee!"), ObjectGenerator.generate(PlayerProfile.class));
    }

    @Test
    public void testInvalidPasswordTooShort() {
        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.PasswordTooShort));
        playerScenarios.register(new PlayerCredential("test@gmail.com", "a"), ObjectGenerator.generate(PlayerProfile.class));
    }

    @Test
    public void testInvalidPasswordTooLong() {
        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.PasswordTooLong));
        playerScenarios.register(
            new PlayerCredential("test@gmail.com", "12345678901234567890123456789012345678901234567890123456789012345678901234567890"), ObjectGenerator.generate(PlayerProfile.class));
    }

    @Test
    public void testInvalidPasswordTooWeak() {
        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.PasswordTooWeak, ClembleCasinoError.PasswordTooShort));
        playerScenarios.register(new PlayerCredential("test@gmail.com", "123456"), ObjectGenerator.generate(PlayerProfile.class));
    }

    @Test
    public void testInvalidPasswordMissing() {
        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.PasswordMissingCode, ClembleCasinoError.PasswordTooWeak));
        playerScenarios.register(new PlayerCredential("test@gmail.com", null), ObjectGenerator.generate(PlayerProfile.class));
    }

}
