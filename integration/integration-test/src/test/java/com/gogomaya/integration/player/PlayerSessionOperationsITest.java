package com.gogomaya.integration.player;

import org.junit.Assert;
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
import com.gogomaya.player.security.PlayerSession;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.integration.player.session.SessionOperations;
import com.gogomaya.server.integration.util.GogomayaExceptionMatcherFactory;
import com.gogomaya.server.spring.integration.TestConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TestConfiguration.class })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class })
public class PlayerSessionOperationsITest {

    @Autowired
    public SessionOperations sessionOperations;

    @Autowired
    public PlayerOperations playerOperations;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testStart() {
        // Step 1. Creating fake player
        Player player = playerOperations.createPlayer();
        // Step 2. When player was created he started session
        PlayerSession currentSession = player.getSession();
        Assert.assertEquals(currentSession, sessionOperations.get(player, currentSession.getSessionId()));
        sessionOperations.end(player, currentSession);
        PlayerSession newSession = sessionOperations.start(player);
        Assert.assertEquals(newSession, sessionOperations.get(player, newSession.getSessionId()));
    }

    @Test
    public void testRefresh() {
        // Step 1. Creating fake player
        Player player = playerOperations.createPlayer();
        // Step 2. When player was created he started session
        PlayerSession currentSession = player.getSession();
        Assert.assertEquals(currentSession, sessionOperations.get(player, currentSession.getSessionId()));
        // Step 3. Refreshing player session
        PlayerSession refreshedSession = sessionOperations.refresh(player, currentSession.getSessionId());
        Assert.assertNotEquals(refreshedSession.getExpirationTime(), currentSession.getExpirationTime());
        Assert.assertEquals(refreshedSession.getPlayer(), currentSession.getPlayer());
        Assert.assertEquals(refreshedSession.getResourceLocations(), currentSession.getResourceLocations());
        Assert.assertEquals(refreshedSession.getSessionId(), currentSession.getSessionId());
    }

    @Test
    public void testEnd() {
        // Step 1. Creating fake player
        Player player = playerOperations.createPlayer();
        // Step 2. When player was created he started session
        PlayerSession currentSession = player.getSession();
        Assert.assertEquals(currentSession, sessionOperations.get(player, currentSession.getSessionId()));
        sessionOperations.end(player, currentSession);
        Assert.assertTrue(sessionOperations.get(player, currentSession.getSessionId()).expired());

        expectedException.expect(GogomayaExceptionMatcherFactory.fromErrors(GogomayaError.PlayerSessionClosed));

        sessionOperations.end(player, currentSession);
    }

    @Test
    public void testEndByAnother() {
        // Step 1. Creating fake player
        Player player = playerOperations.createPlayer();
        Player anotherPlayer = playerOperations.createPlayer();
        // Step 2. When player was created he started session
        PlayerSession currentSession = player.getSession();
        Assert.assertEquals(currentSession, sessionOperations.get(player, currentSession.getSessionId()));

        expectedException.expect(GogomayaExceptionMatcherFactory.fromErrors(GogomayaError.PlayerNotSessionOwner));

        sessionOperations.end(anotherPlayer, currentSession);
    }

    @Test
    public void testGetByAnother() {
        // Step 1. Creating fake player
        Player player = playerOperations.createPlayer();
        Player anotherPlayer = playerOperations.createPlayer();
        // Step 2. When player was created he started session
        PlayerSession currentSession = player.getSession();

        expectedException.expect(GogomayaExceptionMatcherFactory.fromErrors(GogomayaError.PlayerNotSessionOwner));

        Assert.assertEquals(currentSession, sessionOperations.get(anotherPlayer, currentSession.getSessionId()));
    }

}
