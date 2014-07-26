package com.clemble.casino.integration.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.player.PlayerProfileOperations;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.integration.util.ClembleCasinoExceptionMatcherFactory;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.test.random.ObjectGenerator;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { IntegrationTestSpringConfiguration.class })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class })
public class PlayerProfileOperationsITest {

    @Autowired
    public PlayerScenarios playerScenarios;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private PlayerProfile randomProfile() {
        PlayerProfile randomProfile = ObjectGenerator.generate(PlayerProfile.class)
            .setBirthDate(new Date(0))
            .setSocialConnections(new HashSet<ConnectionKey>());
        return randomProfile;
    }

    @Test
    public void testProfileRead() {
        PlayerProfile playerProfile = randomProfile();
        ClembleCasinoOperations player = playerScenarios.createPlayer(playerProfile);
        playerProfile.setPlayer(player.getPlayer());
        PlayerProfileOperations playerProfileOperations = player.profileOperations();
        ClembleCasinoOperations anotherPlayer = playerScenarios.createPlayer();
        PlayerProfileOperations anotherPlayerProfileOperations = anotherPlayer.profileOperations();

        playerProfile.setPlayer(player.getPlayer());
        Assert.assertEquals(playerProfile, player.profileOperations().getPlayerProfile());
        Assert.assertEquals(playerProfile, playerProfileOperations.getPlayerProfile(player.getPlayer()));
        Assert.assertEquals(playerProfile, anotherPlayerProfileOperations.getPlayerProfile(player.getPlayer()));
        assertTrue(player.profileOperations().getPlayerProfile() instanceof PlayerProfile);
    }

    @Test
    public void testMultipleProfileRead() {
        // Step 1. Creating 3 random profiles
        ClembleCasinoOperations A = playerScenarios.createPlayer(randomProfile());
        ClembleCasinoOperations B = playerScenarios.createPlayer(randomProfile());
        ClembleCasinoOperations C = playerScenarios.createPlayer(randomProfile());
        // Step 2. Trying to read 2 profiles B & C from A
        List<PlayerProfile> BCfromA = A.profileOperations().getPlayerProfile(B.getPlayer(), C.getPlayer());
        assertEquals(BCfromA.size(), 2);
        assertTrue(BCfromA.contains(B.profileOperations().getPlayerProfile()));
        assertTrue(BCfromA.contains(C.profileOperations().getPlayerProfile()));
        // Step 2.1 Trying to read 2 profiles A & C from B
        List<PlayerProfile> ACfromB = B.profileOperations().getPlayerProfile(A.getPlayer(), C.getPlayer());
        assertEquals(ACfromB.size(), 2);
        assertTrue(ACfromB.contains(A.profileOperations().getPlayerProfile()));
        assertTrue(ACfromB.contains(C.profileOperations().getPlayerProfile()));
        // Step 2.2 Trying to read 2 profiles A & B from C
        List<PlayerProfile> ABfromC = C.profileOperations().getPlayerProfile(A.getPlayer(), B.getPlayer());
        assertEquals(ABfromC.size(), 2);
        assertTrue(ABfromC.contains(A.profileOperations().getPlayerProfile()));
        assertTrue(ABfromC.contains(B.profileOperations().getPlayerProfile()));
    }

    @Test
    public void testProfileReadNonExistent() {
        ClembleCasinoOperations player = playerScenarios.createPlayer();

        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.PlayerProfileDoesNotExists));

        player.profileOperations().getPlayerProfile("-12345");
    }

    @Test
    public void testProfileWrite() {
        // Step 1. Generating and saving layer with random profile
        PlayerProfile playerProfile = randomProfile();
        // Step 2. Saving profile to DB
        ClembleCasinoOperations player = playerScenarios.createPlayer(playerProfile);
        playerProfile.setPlayer(player.getPlayer());
        PlayerProfileOperations playerProfileOperations = player.profileOperations();
        ClembleCasinoOperations anotherPlayer = playerScenarios.createPlayer();
        PlayerProfileOperations anotherProfileOperations = anotherPlayer.profileOperations();
        Assert.assertEquals(playerProfile, player.profileOperations().getPlayerProfile());
        // Step 3. Updating created player with new Profile value
        PlayerProfile newProfile = randomProfile();
        newProfile.setPlayer(player.getPlayer());
        newProfile.setVersion(0);
        // Step 4. Checking saved profile, replaced the old one
        PlayerProfile savedProfile = player.profileOperations().updatePlayerProfile(newProfile);

        newProfile.setVersion(savedProfile.getVersion());
        Assert.assertEquals(savedProfile, newProfile);

        Assert.assertEquals(newProfile, player.profileOperations().getPlayerProfile());
        Assert.assertEquals(newProfile, playerProfileOperations.getPlayerProfile(player.getPlayer()));
        Assert.assertEquals(newProfile, anotherProfileOperations.getPlayerProfile(player.getPlayer()));
        // Step 5. Repeating steps from 3 to 4 with another new Profile
        newProfile = randomProfile();
        newProfile.setPlayer(player.getPlayer());
        newProfile.setVersion(savedProfile.getVersion());

        savedProfile = playerProfileOperations.updatePlayerProfile(newProfile);

        newProfile.setVersion(savedProfile.getVersion());
        Assert.assertEquals(savedProfile, newProfile);

        Assert.assertEquals(newProfile, player.profileOperations().getPlayerProfile());
        Assert.assertEquals(newProfile, playerProfileOperations.getPlayerProfile(player.getPlayer()));
        Assert.assertEquals(newProfile, playerProfileOperations.getPlayerProfile(player.getPlayer()));
    }

    @Test
    public void testAddIllegalSocial() {
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        PlayerProfile playerProfile = A.profileOperations().getPlayerProfile();
        playerProfile.addSocialConnection(new ConnectionKey("facebook", "323232"));

        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.ProfileSocialCantBeEdited));

        A.profileOperations().updatePlayerProfile(playerProfile);
    }

    @Test
    @Ignore
    public void testProfileWriteByAnother() {
        PlayerProfile playerProfile = randomProfile();
        playerProfile.setPlayer(RandomStringUtils.random(5));

        ClembleCasinoOperations player = playerScenarios.createPlayer(playerProfile);
        ClembleCasinoOperations anotherPlayer = playerScenarios.createPlayer();
        playerProfile.setPlayer(player.getPlayer());
        Assert.assertEquals(playerProfile, player.profileOperations().getPlayerProfile());

        PlayerProfile newProfile = randomProfile();
        newProfile.setPlayer(player.getPlayer());

        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.PlayerNotProfileOwner));

        anotherPlayer.profileOperations().updatePlayerProfile(newProfile);
    }

    @Test
    @Ignore
    // Exception happens before the server invocation, this can be considered as Client problem now
    // TODO restore to valid state
    public void testProfileWriteNull() {
        PlayerProfile playerProfile = randomProfile();
        playerProfile.setPlayer("0");

        ClembleCasinoOperations player = playerScenarios.createPlayer(playerProfile);
        playerProfile.setPlayer(player.getPlayer());
        Assert.assertEquals(playerProfile, player.profileOperations().getPlayerProfile());

        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromPossibleErrors(ClembleCasinoError.PlayerProfileInvalid, ClembleCasinoError.ServerError));

        player.profileOperations().updatePlayerProfile(null);
    }

    @Test
    @Ignore
    // Restore
    public void testProfileWriteWithDifferentId() {
        PlayerProfile playerProfile = randomProfile();
        playerProfile.setPlayer(RandomStringUtils.random(5));

        ClembleCasinoOperations player = playerScenarios.createPlayer(playerProfile);
        playerProfile.setPlayer(player.getPlayer());
        Assert.assertEquals(playerProfile, player.profileOperations().getPlayerProfile());

        PlayerProfile newProfile = randomProfile();
        newProfile.setPlayer(RandomStringUtils.random(5));

        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.PlayerNotProfileOwner));

        Assert.assertEquals(newProfile, player.profileOperations().updatePlayerProfile(newProfile));
    }
}
