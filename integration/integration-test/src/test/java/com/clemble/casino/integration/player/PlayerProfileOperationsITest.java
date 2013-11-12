package com.clemble.casino.integration.player;

import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.clemble.casino.client.player.PlayerProfileOperations;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.integration.spring.TestConfiguration;
import com.clemble.casino.integration.util.ClembleCasinoExceptionMatcherFactory;
import com.clemble.casino.player.NativePlayerProfile;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.test.random.ObjectGenerator;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TestConfiguration.class })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class })
public class PlayerProfileOperationsITest {

    @Autowired
    public PlayerOperations playerOperations;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private NativePlayerProfile randomProfile() {
        NativePlayerProfile randomProfile = ObjectGenerator.generate(NativePlayerProfile.class);
        randomProfile.setImageUrl("http://" + RandomStringUtils.randomAlphabetic(10) + ".com/");
        randomProfile.setBirthDate(new Date(0));
        return randomProfile;
    }

    @Test
    public void testProfileRead() {
        PlayerProfile playerProfile = randomProfile();
        Player player = playerOperations.createPlayer(playerProfile);
        PlayerProfileOperations playerProfileOperations = player.getProfileOperations();
        Player anotherPlayer = playerOperations.createPlayer();
        PlayerProfileOperations anotherPlayerProfileOperations = anotherPlayer.getProfileOperations();

        playerProfile.setPlayer(player.getPlayer());
        Assert.assertEquals(playerProfile, player.getProfile());
        Assert.assertEquals(playerProfile, playerProfileOperations.getPlayerProfile(player.getPlayer()));
        Assert.assertEquals(playerProfile, anotherPlayerProfileOperations.getPlayerProfile(player.getPlayer()));
    }

    @Test
    public void testProfileReadNonExistent() {
        Player player = playerOperations.createPlayer();

        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.PlayerProfileDoesNotExists));

        player.getProfileOperations().getPlayerProfile("-12345");
    }

    @Test
    public void testProfileWrite() {
        // Step 1. Generating and saving layer with random profile
        PlayerProfile playerProfile = randomProfile();
        // Step 2. Saving profile to DB
        Player player = playerOperations.createPlayer(playerProfile);
        playerProfile.setPlayer(player.getPlayer());
        PlayerProfileOperations playerProfileOperations = player.getProfileOperations();
        Player anotherPlayer = playerOperations.createPlayer();
        PlayerProfileOperations anotherProfileOperations = anotherPlayer.getProfileOperations();
        Assert.assertEquals(playerProfile, player.getProfile());
        // Step 3. Updating created player with new Profile value
        NativePlayerProfile newProfile = randomProfile();
        newProfile.setPlayer(player.getPlayer());
        newProfile.setVersion(0);
        // Step 4. Checking saved profile, replaced the old one
        PlayerProfile savedProfile = player.getProfileOperations().updatePlayerProfile(newProfile);

        newProfile.setVersion(savedProfile.getVersion());
        Assert.assertEquals(savedProfile, newProfile);

        Assert.assertEquals(newProfile, player.getProfile());
        Assert.assertEquals(newProfile, playerProfileOperations.getPlayerProfile(player.getPlayer()));
        Assert.assertEquals(newProfile, anotherProfileOperations.getPlayerProfile(player.getPlayer()));
        // Step 5. Repeating steps from 3 to 4 with another new Profile
        newProfile = randomProfile();
        newProfile.setPlayer(player.getPlayer());
        newProfile.setVersion(savedProfile.getVersion());

        savedProfile = playerProfileOperations.updatePlayerProfile(newProfile);

        newProfile.setVersion(savedProfile.getVersion());
        Assert.assertEquals(savedProfile, newProfile);

        Assert.assertEquals(newProfile, player.getProfile());
        Assert.assertEquals(newProfile, playerProfileOperations.getPlayerProfile(player.getPlayer()));
        Assert.assertEquals(newProfile, playerProfileOperations.getPlayerProfile(player.getPlayer()));
    }

    @Test
    @Ignore
    // TODO Test security
    public void testProfileWriteByAnother() {
        PlayerProfile playerProfile = randomProfile();
        playerProfile.setPlayer(RandomStringUtils.random(5));

        Player player = playerOperations.createPlayer(playerProfile);
        Player anotherPlayer = playerOperations.createPlayer();
        playerProfile.setPlayer(player.getPlayer());
        Assert.assertEquals(playerProfile, player.getProfile());

        PlayerProfile newProfile = randomProfile();
        newProfile.setPlayer(player.getPlayer());

        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.PlayerNotProfileOwner));

        //TODO restore Assert.assertEquals(newProfile, playerProfileOperations.put(anotherPlayer, player.getPlayer(), newProfile));
    }

    @Test
    @Ignore
    // Exception happens before the server invocation, this can be considered as Client problem now
    // TODO restore to valid state
    public void testProfileWriteNull() {
        PlayerProfile playerProfile = randomProfile();
        playerProfile.setPlayer("0");

        Player player = playerOperations.createPlayer(playerProfile);
        playerProfile.setPlayer(player.getPlayer());
        Assert.assertEquals(playerProfile, player.getProfile());

        PlayerProfile newProfile = null;

        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromPossibleErrors(ClembleCasinoError.PlayerProfileInvalid,
                ClembleCasinoError.ServerError));

        // TODO restore Assert.assertEquals(newProfile, playerProfileOperations.put(player, player.getPlayer(), newProfile));
    }

    @Test
    @Ignore // Restore
    public void testProfileWriteWithDifferentId() {
        PlayerProfile playerProfile = randomProfile();
        playerProfile.setPlayer(RandomStringUtils.random(5));

        Player player = playerOperations.createPlayer(playerProfile);
        playerProfile.setPlayer(player.getPlayer());
        Assert.assertEquals(playerProfile, player.getProfile());

        PlayerProfile newProfile = randomProfile();
        newProfile.setPlayer(RandomStringUtils.random(5));

        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.PlayerNotProfileOwner));

        Assert.assertEquals(newProfile, player.getProfileOperations().updatePlayerProfile(newProfile));
    }
}
