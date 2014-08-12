package com.clemble.casino.integration.player;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.player.PlayerConnections;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Collection;

/**
 * Created by mavarazy on 7/31/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { IntegrationTestSpringConfiguration.class })
public class PlayerConnectionsITest {

    @Autowired
    public PlayerScenarios playerScenarios;

    @Test
    public void testEmptyConnections() {
        final ClembleCasinoOperations A = playerScenarios.createPlayer();
        final PlayerConnections aConnections = A.connectionOperations().myConnections();
        Assert.assertNotNull(aConnections);
        Assert.assertEquals(aConnections.getOwned().size(), 0);
        Assert.assertEquals(aConnections.getConnected().size(), 0);
    }

}
