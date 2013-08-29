package com.gogomaya.server.web.player.session;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public PlayerSessionController playerSessionController;

    @Autowired
    public PlayerStateManager playerStateManager;

    @Test
    public void testPlayerAvailable() {
        PlayerSession playerSession = playerSessionController.post(9, 9);
        Assert.assertNotNull(playerSession);
        Assert.assertEquals(playerSession.getPlayerId(), 9);
        Assert.assertTrue(playerStateManager.isAvailable(9));
        Assert.assertTrue(playerStateManager.isAvailable(9));
        Assert.assertTrue(playerStateManager.areAvailable(Collections.singleton(Long.valueOf(9))));
    }
}
