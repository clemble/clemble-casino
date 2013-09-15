package com.gogomaya.integration.player;

import java.util.Date;

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

import com.gogomaya.error.GogomayaError;
import com.gogomaya.player.PlayerProfile;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.integration.player.profile.ProfileOperations;
import com.gogomaya.server.integration.util.GogomayaExceptionMatcherFactory;
import com.gogomaya.server.spring.integration.TestConfiguration;
import com.stresstest.random.ObjectGenerator;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TestConfiguration.class })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class })
public class PlayerProfileOperationsITest {

    @Autowired
    public ProfileOperations playerProfileOperations;

    @Autowired
    public PlayerOperations playerOperations;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private PlayerProfile randomProfile() {
        PlayerProfile randomProfile = ObjectGenerator.generate(PlayerProfile.class);
        randomProfile.setImageUrl("http://" + randomProfile.getImageUrl() + ".com/");
        randomProfile.setBirthDate(new Date(0));
        return randomProfile;
    }

    @Test
    public void testProfileRead() {
        PlayerProfile playerProfile = randomProfile();
        Player player = playerOperations.createPlayer(playerProfile);
        Player anotherPlayer = playerOperations.createPlayer();

        playerProfile.setPlayerId(player.getPlayerId());
        Assert.assertEquals(playerProfile, player.getProfile());
        Assert.assertEquals(playerProfile, playerProfileOperations.get(player, player.getPlayerId()));
        Assert.assertEquals(playerProfile, playerProfileOperations.get(anotherPlayer, player.getPlayerId()));
    }

    @Test
    public void testProfileReadNonExistent() {
        Player player = playerOperations.createPlayer();

        expectedException.expect(GogomayaExceptionMatcherFactory.fromErrors(GogomayaError.PlayerProfileDoesNotExists));

        playerProfileOperations.get(player, -1);
    }

    @Test
    public void testProfileWrite() {
        PlayerProfile playerProfile = randomProfile();

        Player player = playerOperations.createPlayer(playerProfile);
        playerProfile.setPlayerId(player.getPlayerId());
        Player anotherPlayer = playerOperations.createPlayer();
        Assert.assertEquals(playerProfile, player.getProfile());

        PlayerProfile newProfile = randomProfile();
        newProfile.setPlayerId(player.getPlayerId());

        Assert.assertEquals(newProfile, playerProfileOperations.put(player, player.getPlayerId(), newProfile));

        Assert.assertEquals(newProfile, player.getProfile());
        Assert.assertEquals(newProfile, playerProfileOperations.get(player, player.getPlayerId()));
        Assert.assertEquals(newProfile, playerProfileOperations.get(anotherPlayer, player.getPlayerId()));
    }

    @Test
    @Ignore //Test security
    public void testProfileWriteByAnother() {
        PlayerProfile playerProfile = randomProfile();
        playerProfile.setPlayerId(0);

        Player player = playerOperations.createPlayer(playerProfile);
        Player anotherPlayer = playerOperations.createPlayer();
        playerProfile.setPlayerId(player.getPlayerId());
        Assert.assertEquals(playerProfile, player.getProfile());

        PlayerProfile newProfile = randomProfile();
        newProfile.setPlayerId(player.getPlayerId());

        expectedException.expect(GogomayaExceptionMatcherFactory.fromErrors(GogomayaError.PlayerNotProfileOwner));

        Assert.assertEquals(newProfile, playerProfileOperations.put(anotherPlayer, player.getPlayerId(), newProfile));
    }

    @Test
    public void testProfileWriteNull() {
        PlayerProfile playerProfile = randomProfile();
        playerProfile.setPlayerId(0);

        Player player = playerOperations.createPlayer(playerProfile);
        playerProfile.setPlayerId(player.getPlayerId());
        Assert.assertEquals(playerProfile, player.getProfile());

        PlayerProfile newProfile = null;

        expectedException.expect(GogomayaExceptionMatcherFactory.fromPossibleErrors(GogomayaError.PlayerProfileInvalid, GogomayaError.ServerError));

        Assert.assertEquals(newProfile, playerProfileOperations.put(player, player.getPlayerId(), newProfile));
    }

    @Test
    public void testProfileWriteWithDifferentId() {
        PlayerProfile playerProfile = randomProfile();
        playerProfile.setPlayerId(0);

        Player player = playerOperations.createPlayer(playerProfile);
        playerProfile.setPlayerId(player.getPlayerId());
        Assert.assertEquals(playerProfile, player.getProfile());

        PlayerProfile newProfile = randomProfile();
        newProfile.setPlayerId(player.getPlayerId() + 10);

        expectedException.expect(GogomayaExceptionMatcherFactory.fromErrors(GogomayaError.PlayerNotProfileOwner));

        Assert.assertEquals(newProfile, playerProfileOperations.put(player, player.getPlayerId(), newProfile));
    }
}
