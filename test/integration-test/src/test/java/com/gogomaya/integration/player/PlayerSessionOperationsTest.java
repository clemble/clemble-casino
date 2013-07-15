package com.gogomaya.integration.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.integration.player.session.SessionOperations;
import com.gogomaya.server.integration.util.GogomayaErrorMatcher;
import com.gogomaya.server.player.security.PlayerSession;
import com.gogomaya.server.spring.integration.TestConfiguration;
import com.gogomaya.server.test.RedisCleaner;
import com.stresstest.util.CollectionAssert;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TestConfiguration.class })
@TestExecutionListeners(listeners = { RedisCleaner.class, DependencyInjectionTestExecutionListener.class })
public class PlayerSessionOperationsTest {

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
        Assert.assertEquals(refreshedSession.getPlayerId(), currentSession.getPlayerId());
        Assert.assertEquals(refreshedSession.getServer(), currentSession.getServer());
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

        expectedException.expect(GogomayaErrorMatcher.create(GogomayaError.PlayerSessionClosed));

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

        expectedException.expect(GogomayaErrorMatcher.create(GogomayaError.PlayerNotSessionOwner));

        sessionOperations.end(anotherPlayer, currentSession);
    }

    @Test
    public void testGetByAnother() {
        // Step 1. Creating fake player
        Player player = playerOperations.createPlayer();
        Player anotherPlayer = playerOperations.createPlayer();
        // Step 2. When player was created he started session
        PlayerSession currentSession = player.getSession();

        expectedException.expect(GogomayaErrorMatcher.create(GogomayaError.PlayerNotSessionOwner));

        Assert.assertEquals(currentSession, sessionOperations.get(anotherPlayer, currentSession.getSessionId()));
    }

    @Test
    public void testList() {
        // Step 1. Creating fake player
        Player player = playerOperations.createPlayer();
        // Step 2. When player was created he started session
        Collection<PlayerSession> expectedSessions = new ArrayList<>();
        expectedSessions.add(player.getSession());
        List<PlayerSession> actualSessions = new ArrayList<>(sessionOperations.list(player));
        Assert.assertNotNull(actualSessions);
        Assert.assertTrue(!actualSessions.isEmpty());
        CollectionAssert.assertContent(expectedSessions, sessionOperations.list(player));
        // Step 3. Generating list of sessions
        for (int i = 0; i < 100; i++)
            expectedSessions.add(sessionOperations.start(player));
        // Step 4. Check current player can read sessions
        CollectionAssert.assertContent(expectedSessions, new ArrayList<>(sessionOperations.list(player)));
    }

}
