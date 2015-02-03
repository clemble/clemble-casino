package com.clemble.casino.integration.player;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.integration.ClembleIntegrationTest;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Set;

/**
 * Created by mavarazy on 7/31/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ClembleIntegrationTest
public class PlayerConnectionsITest {

    @Autowired
    public PlayerScenarios playerScenarios;

    @Test
    public void testEmptyConnections() {
        final ClembleCasinoOperations A = playerScenarios.createPlayer();
        final Set<String> aConnections = A.connectionOperations().myConnections();
        Assert.assertNotNull(aConnections);
        Assert.assertEquals(aConnections.size(), 0);
    }

}
