package com.gogomaya.server.web.player.session;

import java.util.Collections;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gogomaya.player.security.PlayerSession;
import com.gogomaya.server.player.state.PlayerStateManager;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.web.player.PlayerWebSpringConfiguration;
import com.gogomaya.server.web.player.PlayerSessionController;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles(SpringConfiguration.UNIT_TEST)
@ContextConfiguration(classes = { PlayerWebSpringConfiguration.class })
public class PlayerSessionControllerTest {

    @Inject
    public PlayerSessionController playerSessionController;

    @Inject
    public PlayerStateManager playerStateManager;

    @Test
    public void testPlayerAvailable() {
        PlayerSession playerSession = playerSessionController.create(9);
        Assert.assertNotNull(playerSession);
        Assert.assertEquals(playerSession.getPlayerId(), 9);
        Assert.assertTrue(playerStateManager.isAvailable(9));
        Assert.assertTrue(playerStateManager.isAvailable(9));
        Assert.assertTrue(playerStateManager.areAvailable(Collections.singleton(Long.valueOf(9))));
    }
}
