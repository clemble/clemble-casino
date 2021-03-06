package com.clemble.casino.integration.player;

import com.clemble.casino.integration.ClembleIntegrationTest;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.player.service.PlayerSessionService;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.test.util.ClembleCasinoExceptionMatcherFactory;
import com.clemble.casino.player.PlayerSession;

@RunWith(SpringJUnit4ClassRunner.class)
@ClembleIntegrationTest
public class PlayerSessionOperationsITest {

    @Autowired
    public PlayerScenarios playerOperations;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testStart() {
        // Step 1. Creating fake player
        ClembleCasinoOperations player = playerOperations.createPlayer();
        PlayerSessionService sessionOperations = player.sessionOperations();
        // Step 2. When player was created he started session
        PlayerSession currentSession = sessionOperations.create();
        Assert.assertEquals(currentSession, sessionOperations.getPlayerSession(currentSession.getSessionId()));
        sessionOperations.endPlayerSession(currentSession.getSessionId());
        PlayerSession newSession = sessionOperations.create();
        Assert.assertEquals(newSession, sessionOperations.getPlayerSession(newSession.getSessionId()));
    }

    @Test
    public void testRefresh() {
        // Step 1. Creating fake player
        ClembleCasinoOperations player = playerOperations.createPlayer();
        PlayerSessionService sessionOperations = player.sessionOperations();
        // Step 2. When player was created he started session
        PlayerSession currentSession = sessionOperations.create();
        Assert.assertEquals(currentSession, sessionOperations.getPlayerSession(currentSession.getSessionId()));
        // Step 3. Refreshing player session
        PlayerSession refreshedSession = sessionOperations.refreshPlayerSession(currentSession.getSessionId());
        Assert.assertNotEquals(refreshedSession.getExpirationTime(), currentSession.getExpirationTime());
        Assert.assertEquals(refreshedSession.getPlayer(), currentSession.getPlayer());
        Assert.assertEquals(refreshedSession.getSessionId(), currentSession.getSessionId());
    }

    @Test
    public void testEnd() {
        // Step 1. Creating fake player
        ClembleCasinoOperations player = playerOperations.createPlayer();
        PlayerSessionService sessionOperations = player.sessionOperations();
        // Step 2. When player was created he started session
        PlayerSession currentSession = sessionOperations.create();
        Assert.assertEquals(currentSession, sessionOperations.getPlayerSession(currentSession.getSessionId()));
        sessionOperations.endPlayerSession(currentSession.getSessionId());
        Assert.assertTrue(sessionOperations.getPlayerSession(currentSession.getSessionId()).expired());

        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.PlayerSessionClosed));

        sessionOperations.endPlayerSession(currentSession.getSessionId());
    }
/** Test
    @Test
    public void testEndByAnother() {
        // Step 1. Creating fake player
        ClembleCasinoOperations player = playerOperations.createPlayer();
        PlayerSessionOperations sessionOperations = player.sessionOperations();
        ClembleCasinoOperations anotherPlayer = playerOperations.createPlayer();
        // Step 2. When player was created he started session
        PlayerSession currentSession = player.getSessionKey();
        Assert.assertEquals(currentSession, sessionOperations.getPlayerSession(currentSession.getSessionId()));

        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.PlayerNotSessionOwner));

        sessionOperations.endPlayerSession(anotherPlayer.getSessionKey().getSessionId());
    }

    @Test
    public void testGetByAnother() {
        // Step 1. Creating fake player
        ClembleCasinoOperations player = playerOperations.createPlayer();
        PlayerSessionOperations sessionOperations = player.sessionOperations();
        ClembleCasinoOperations anotherPlayer = playerOperations.createPlayer();
        // Step 2. When player was created he started session
        PlayerSession currentSession = player.getSessionKey();

        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.PlayerNotSessionOwner));

        Assert.assertEquals(currentSession, sessionOperations.getPlayerSession(anotherPlayer.getSessionKey().getSessionId()));
    }
*/
}
